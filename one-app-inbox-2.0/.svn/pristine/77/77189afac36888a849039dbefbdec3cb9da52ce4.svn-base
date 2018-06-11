package org.grails.plugins.excelimport
/**
 * Created by IntelliJ IDEA.
 * User: Jean Barmash
 * Date: Sep 28, 2009
 * Time: 6:30:17 PM
 */

import org.apache.poi.hssf.usermodel.*
import org.apache.poi.hssf.util.CellReference

public abstract class AbstractExcelImporter {
  String inFile = null
  InputStream inStr = null
  HSSFWorkbook workbook = null
  HSSFSheet sheet = null

  HSSFFormulaEvaluator evaluator = null;
  public AbstractExcelImporter(String fileName) {
    this.inFile = fileName
    inStr = new FileInputStream(inFile)
    workbook = new HSSFWorkbook(inStr)
    evaluator = new HSSFFormulaEvaluator(workbook)
  }

  def close() {
    inStr.close()
  }

}