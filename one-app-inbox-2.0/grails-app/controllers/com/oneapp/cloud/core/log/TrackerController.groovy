

package com.oneapp.cloud.core.log

class TrackerController {

    def index = { redirect(action: "list", params: params) }


    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 50, 100)
        if ( params.sort == null ) {
        	params.sort = 'dateCreated'
        	params.order = 'desc'
        }
		
		def trackerList =  Tracker.createCriteria().list(sort:params.sort,offset:params.offset,max:params.max,order:params.order){
				if(params.tenantId)
					eq 'clientID',params.tenantId
			}
        [trackerInstanceList: trackerList, trackerInstanceTotal: trackerList.totalCount,tenantId:params.tenantId]
    }
}
