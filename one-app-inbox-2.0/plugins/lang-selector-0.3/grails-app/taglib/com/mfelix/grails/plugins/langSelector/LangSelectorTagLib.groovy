package com.mfelix.grails.plugins.langSelector

class LangSelectorTagLib {

	static namespace = "langs"
		
	def selector = {attrs ->
		def values = null
		try{
			values = new ArrayList(attrs.langs.split(",").toList())
		}
		catch(Exception e){
			log.error "Error getting value of required attribute 'langs'", e
			throw new Exception("Error getting value of required attribute 'langs'. Accepted value for example is: es,en_US,en")
		}
		def selected = session["org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE"]
		// if not set in session, get it from attrs
		selected = selected?selected:attrs.default?.trim()
		//if no default is set get default locale
		selected = selected?selected:Locale.getDefault()
		String url = attrs.url
		if(url == null ){ 
			url = request.getRequestURI() + '?'
			def query = request.getQueryString()? request.getQueryString().replace('lang='+selected.toString(),'') : ''
			if(query != '' && !query.endsWith('&'))
				query += '&' 
			url += query +'lang='
		}
		else{
			if(url.indexOf('?') > 0)
				url += '&lang='
			else
				url += '?lang='
		}
		def supported = StaticConfig.config()
		def flags = [:]
		values.each { 
			def language = it.indexOf("_")>0? it.substring(0,it.indexOf("_")):it
		    def country =  it.indexOf("_")>0? it.substring(it.indexOf("_")+1,it.length()):supported[language.toLowerCase().trim()]
		    if(country)
		    	flags[it.trim()] = country.toLowerCase().trim()
		    else
		    	log.error "No country flag found for:"+language+" please check configuration."
		}
		//distincion de seleccionado o no por defecto estilo opacity
		def selected_class = ''
		def not_selected_class = 'opacitiy_not_selected'
		
		out << render( template:'/langSelector/selector', plugin:'langSelector',
			model:[flags:flags,
			       selected_class:selected_class,
			       not_selected_class:not_selected_class,
			       selected:selected,
				   uri:url
			       ])
	}
	
	def resources = {
		out << """
		<link rel='stylesheet' href="${resource(plugin:'langSelector',dir:'css',file:'langSelector.css')}" />
		"""
	}
	
}
