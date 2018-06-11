package com.oneapp.cloud.core


class EmailService {

    static transactional = false
      static expose = ['jms']


    def sendEmail(attrs) {
        sendJMSMessage("sendMail",
                [to: attrs.to,
                 from: attrs.from,
                 subject: attrs.subject,
                 view: attrs.view,
                 model: attrs.model])
    }
}
