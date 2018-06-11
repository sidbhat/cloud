package org.grails.plugins.excelimport

import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.util.CellReference
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.joda.time.LocalDate
import org.apache.commons.logging.Log
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.commons.logging.LogFactory

/**
 * Created by IntelliJ IDEA.
 * User: Jean Barmash
 * Date: Sep 29, 2009
 * Time: 5:08:39 PM
 */

public class ExcelImportUtils {

  static int PROPERTY_TYPE_INT = 1
  static int PROPERTY_TYPE_STRING = 2
  static int PROPERTY_TYPE_DATE = 3

  /* Looks through supplied index (columnIndex), and matches a siteID in that column, returning the row of the match
  */
  static HSSFRow findRowById(HSSFSheet sheet, String siteId, int columnIndex) {
    for (HSSFRow r: sheet){
      if (row.getCell(columnIndex)?.stringCellValue==siteId) {
        log.info "found ID $siteId"
        return r
      }
    }
    return null
  }

  /* receives a list of column names, i.e. ['Site', 'ID'], and tries to find the indexes of the columns matching those names
  * returning a map, i.e. ['Site':2, 'ID':8]
  */
  static Map createColumnMapFromColumnNames(List COLUMN_NAMES, HSSFRow headerRow) {
    def columnIndexMap = [:]

    for (HSSFCell c: headerRow) {
      if (COLUMN_NAMES.contains(c.stringCellValue)) {
        log.debug "Found index for column ${c.stringCellValue}"
        columnIndexMap[c.stringCellValue] = c.columnIndex
      }
    }

    log.info "columnIndexMap $columnIndexMap"
    columnIndexMap
  }

  static Log log = LogFactory.getLog(ExcelImportUtils.class)


  static def convertColumnMapConfigManyRows(HSSFWorkbook workbook, Map config, HSSFFormulaEvaluator evaluator = null, propertyConfigurationMap = null, int lastRow = -1) {
    def sheet = workbook.getSheet(config.sheet)
    return ExcelImportUtils.convertColumnMapManyRows(sheet, config.columnMap, config.startRow, evaluator, propertyConfigurationMap, lastRow)
  }  


/**
   * Receives sheet, offset, and map of columns.  Map is between a column name (i.e. B) and what will become returning map key.
   * For example, columnMap could be ['B':'endDate', 'D':cost], which will cause going down the B and D columns and retrieving values
   * packaging up as maps to be returned, in this case something like [[endDate:LocalDate(2009/1/2), cost:30], [endDate: LocalDate(2009,1,3), cost:20]]
   * This method is very generic, and could be used by anything
   */
  static def convertColumnMapManyRows(HSSFSheet currentSheet, Map columnMap, int firstRow, HSSFFormulaEvaluator evaluator = null, propertyConfigurationMap = null, int lastRow = -1) {

    if (currentSheet==null) return []
    boolean foundBlankRow = false
    def returnList = []
    for (int rowIndex=firstRow; (rowIndex < lastRow || ((lastRow==-1)) && !foundBlankRow); rowIndex++) {
      //println "ColumnMap $columnMap"
      Map returnParams = convertColumnMapOneRow(currentSheet, columnMap, rowIndex, evaluator, propertyConfigurationMap)
      //println "returning $returnParams"
      if (!returnParams) foundBlankRow = true
      log.debug "Index $rowIndex Result map values $returnParams"
      //println "Index $rowIndex Result map values $returnParams"
      if (!foundBlankRow) returnList << returnParams
    }
    returnList
  }

  //Form of sitePropertyConfigurationMap:  ['totalSquareFeet':[type:"integer", defaultValue:-1]]
  static Map convertColumnMapOneRow(HSSFSheet currentSheet, Map columnMap, int rowIndex, HSSFFormulaEvaluator evaluator = null, propertyConfigurationMap = null ) {
    //println "propertyConfig $propertyConfigurationMap"
    def returnParams = [:]
    def row = currentSheet.getRow(rowIndex)
    if (!row) {
      log.info "Row is null at row $rowIndex, sheet ${currentSheet.sheetName}" 
      return returnParams
    }
    columnMap.each { columnName, propertyName ->
      try {
        def value = getCellValueByColName(row, columnName, evaluator, propertyConfigurationMap?.get(propertyName))
        log.trace "\t\tValue for $propertyName (column ${columnName}) is $value "
        if (value==null || ''.equals(value)) { //cheking for null, because otherwise 0 value will fail here
          log.info "Value for column $columnName row $rowIndex is null or empty String.  Was trying to set $propertyName.  Skipping setting its value in param map"
        } else {
          returnParams[propertyName] = value
        }
      } catch (Exception e) {
        log.warn "Exception caught at row $rowIndex column $columnName while trying to set propertyName $propertyName", e
        //continue in the loop, so can collect other properties
      }
    }
    returnParams
  }

static def getCellValueForSheetNameAndCell(HSSFWorkbook workbook, String sheetName, String cellName) {
  def sheet = workbook.getSheet(sheetName)
  if (!sheet) return null
  HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook)
       try {
        def cell = getCell(sheet, cellName)
        def value = getCellValue(cell, evaluator, null)
        log.debug "\t\tValue for $cellName is $value "
         return value
     } catch (Exception e) {
          log.error "Exception in cell $cellName thrown while getting cell values",  e
          //println "Exception in cell $cellName thrown while getting cell values $e"
         return null
       }
}


  static def convertFromCellMapToMapWithValues(HSSFWorkbook workbook, Map config, Map propertyConfigurationMap = [:]) {
    def sheet = workbook.getSheet(config.sheet )
    if (!sheet) throw new IllegalArgumentException("Did not find sheet named ${config.sheet}")
    convertFromCellMapToMapWithValues( sheet, config.cellMap, propertyConfigurationMap) 
  }


  //totalSquareFeet:([expectedType: ExcelImportUtils.PROPERTY_TYPE_INT, defaultValue:null]),
  static def convertFromCellMapToMapWithValues(HSSFSheet currentSheet, Map cellMap, Map propertyConfigurationMap = [:]) {
     Map objectParams = [:]

     HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(currentSheet.getWorkbook())
     cellMap.each { String cellName, String propertyName ->
       try {
        def cell = getCell(currentSheet, cellName)
        def value = getCellValue(cell, evaluator, propertyConfigurationMap[propertyName])
        //println "\t\tValue for $propertyName is $value "
         log.debug "\t\tValue for $propertyName is $value "
        if (!value) {
            log.warn "No value in cell $cellName set.  Was trying to set $propertyName"
            //println "No value in cell $cellName set.  Was trying to set $propertyName"
        } else {
          objectParams[propertyName] =  value
        }

       } catch (Exception e) {
          log.error "Exception in cell $cellName getting $propertyName thrown while getting cell values",  e
          //println "Exception in cell $cellName getting $propertyName thrown while getting cell values $e"
       }
      }
    log.debug "Returning objectParams $objectParams"
    objectParams
  }

  static Serializable getCellValueByColName(HSSFRow row, String columnName, HSSFFormulaEvaluator evaluator = null, Map propertyConfiguration = null) {
    int colIndex = CellReference.convertColStringToIndex(columnName)
    log.debug "getCellValueByColName $columnName row ${row.rowNum}, propConfig $propertyConfiguration, colIndex = $colIndex"
    HSSFCell cell = row.getCell(colIndex)
    //println "\t\t\tCell is $cell"
    getCellValue(cell, evaluator, propertyConfiguration)
  }

  static def getCellValueByCellName(HSSFSheet currentSheet, String cellName, HSSFFormulaEvaluator evaluator = null) {
    if (evaluator==null) {
      evaluator = new HSSFFormulaEvaluator(currentSheet.getWorkbook())
    }
    def cell = getCell(currentSheet, cellName)
    getCellValue(cell, evaluator)
  }



  /**
   * Returns date or string, or numeric, depending on what's in the cell
   */
   static Serializable getCellValue(HSSFCell origcell, HSSFFormulaEvaluator evaluator = null, propertyConfiguration = null) {
     HSSFCell cell = evaluator? evaluator.evaluateInCell(origcell): origcell; //evaluates formula and returns value
     //HSSFCell cell = evaluator? evaluator.evaluateFormulaCellValue()

    if (!cell) return null
    switch (cell.cellType) {
      case HSSFCell.CELL_TYPE_STRING:
        //println "string cell $origcell"
        //println "string cell propertyConfig $propertyConfiguration"
        if (propertyConfiguration && (propertyConfiguration.expectedType!=PROPERTY_TYPE_STRING))  {
          log.warn "Potential issue - the excel file claims the type is String, but expecting something else for cell with value $origcell. Returning default value of ${propertyConfiguration.defaultValue}"
          //println "Potential issue - the excel file claims the type is String, but expecting something else for cell with value $origcell. Returning default value of ${propertyConfiguration.defaultValue}"

          if (propertyConfiguration.expectedType==PROPERTY_TYPE_DATE)  {
            return propertyConfiguration.defaultValue
          }
          if (propertyConfiguration.expectedType==PROPERTY_TYPE_INT)  {
            log.warn "Expected Type is INT, Trying to extract numeric value"
            try {
              return cell.stringCellValue.toInteger()
            } catch (Exception e) {
              log.warn "Cannot get numeric value, returning default value specified for this type", e
              return propertyConfiguration.defaultValue
            }
          }
          return propertyConfiguration.defaultValue
        }
        String strValue = cell.stringCellValue
        if (propertyConfiguration && strValue == propertyConfiguration.valueEquivalentToNull) {
          log.info ("Found a value that's not null (value ${strValue}), but configuration property says to return null anyway")
          return null
        }
        //log.warn "Encountered unexpected string cell Value ${cell.stringCellValue} at row ${cell.getRowIndex()} column ${cell.getColumnIndex()}"
        return strValue
        break;
      case HSSFCell.CELL_TYPE_NUMERIC:
        //println "numeric cell $origcell"
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
          return new LocalDate(cell.dateCellValue)
        } else {
          return cell.numericCellValue
        }
        break;
    case HSSFCell.CELL_TYPE_ERROR:
      log.warn "CELL is ERROR $cell.errorCellValue"
      return null
    case HSSFCell.CELL_TYPE_FORMULA:
      log.warn "Cell type is formula, returning null"
      return null
    case HSSFCell.CELL_TYPE_BOOLEAN:
      return cell.booleanCellValue
      case HSSFCell.CELL_TYPE_BLANK:
        log.debug "Found blank cell at row ${cell.getRowIndex()}  column ${cell.getColumnIndex()}"
        return null;
      default:
        log.warn "Unexpected cell type.  Ignoring.  Cell Value [${cell}] type ${cell.cellType}"
    }
    log.error "WARNING: RETURNING NULL FROM getCellValue.  UNEXPECTED CONDITION"
    return null;
  }



  static HSSFCell getCell(HSSFSheet currentSheet, String ref) {
    CellReference cellReference = new CellReference(ref);
    HSSFRow row = currentSheet.getRow(cellReference.getRow());
    HSSFCell cell = row.getCell(cellReference.getCol())
    //println "returning cell $cell"
    cell
  }



}
