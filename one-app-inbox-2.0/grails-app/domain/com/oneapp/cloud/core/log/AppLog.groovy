 
package com.oneapp.cloud.core.log

import java.io.Serializable;

import com.oneapp.cloud.core.User

class AppLog implements Serializable{

    String ip
    String uri
    User user
    Date dateCreated
    Date lastUpdated
    String msg
    String deviceType
    String msgType

    static constraints = {
        msg blank: true, nullable: true, maxSize:2056
        msgType blank: true, nullable: true,inList:["E", "I", "W"] //error info or warning
        deviceType blank: false, nullable: false
        ip(size: 2..125, nullable: false)
        uri(size: 2..256, nullable: false)
    }


}