 
package com.oneapp.cloud.core.log

import com.oneapp.cloud.core.User

class LogEventService {

    static transactional = false

    static log(String ip, String uri, User user) {

        AppLog alog = new AppLog()
        alog.ip = ip
        alog.uri = uri
        alog.user = user
        alog.save()
    }


}