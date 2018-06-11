 
package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class DropDown implements Serializable{
    DropDownTypes type
    String name
    String description

    static constraints = {
        type blank: false, nullable: false
        name blank: false, nullable: false
        description blank: false, nullable: false
    }

    String toString() {
        description
    }


}

enum DropDownTypes
{
    STATUS('STATUS'),
    TASK_TYPE('TASK_TYPE'),
    EMPLOYEE_TYPE('EMPLOYEE_TYPE'),
    GROUP_TYPE('GROUP_TYPE'), // company, personal, department, other
    UPDATE_TYPE('UPDATE_TYPE'), // add, update, delete, due-date
    SHARE_TYPE('SHARE_TYPE'),  // Share with all,group or private
	SUB_ORG('SUB_ORG'),
	DEPARTMENT('DEPARTMENT')
	
    String description

    DropDownTypes(String description) {
        this.description = description
    }

    String toString() {

        return description

    }
}