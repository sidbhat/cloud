package com.mfelix.grails.plugins.langSelector

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class StaticConfig {

	//this static property can be overriden by config 
	static langFlags = ["es":"es",
	                    "en":"gb",
	                    "fr":"fr",
	                    "da":"dk",
	                    "de":"de",
	                    "it":"it",
	                    "ja":"jp",
	                    "nl":"nl",
	                    "ru":"ru",
	                    "th":"th",
	                    "zh":"cn",
	                    "pt":"pt"
	                    ]
	                    
	                    //com.mfelix.grails.plugins.langSelector
	static def config(){
		CH.config.com.mfelix.grails.plugins.langSelector.lang.flags?
				CH.config.com.mfelix.grails.plugins.langSelector.lang.flags:langFlags
	} 
}
