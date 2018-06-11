 
package com.oneapp.cloud.core

import java.io.Serializable;

class BaseApproval extends Base implements Serializable{

    User createdBy
    User updatedBy
    User approvedBy
    Date approvedDate
    String note

    static constraints = {
        createdBy blank: true, nullable: true
        updatedBy blank: true, nullable: true
        approvedBy blank: true, nullable: true
        approvedDate blank: true, nullable: true
        note blank: true, nullable: true
    }


}
