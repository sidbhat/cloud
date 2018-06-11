 
package com.oneapp.cloud.core

import org.grails.comments.Commentable
import org.grails.taggable.Taggable
import java.io.Serializable;

class Base implements Taggable, Commentable, Serializable{

    Date dateCreated
    Date lastUpdated
    boolean active
    boolean archived
    DropDown status

    static mapping = {
        tablePerHierarchy false
    }

    static constraints = {
        active blank: true, nullable: true
        archived blank: true, nullable: true
        status blank: true, nullable: true
    }

    def beforeInsert = {
        dateCreated = new Date()
        active = true
    }
    def beforeUpdate = {
        lastUpdated = new Date()
    }

}
