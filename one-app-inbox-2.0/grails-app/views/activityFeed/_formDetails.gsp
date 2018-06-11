<%@ page
	import="org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events"%>
<%@ page
	import="org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator"%>
<%@ page import="org.codehaus.groovy.grails.plugins.PluginManagerHolder"%>
<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder"%>
<%@ page import="org.grails.formbuilder.*"%>
<%@ page import="grails.converters.JSON"%>
<!-- Add details for the object..Move this to a taglib-->
<form:drawForm className="${className}" notiForm="${notiForm}" shareId="${shareId}" notiDI="${notiDI }" shareType="${shareType}" activityFeedId="${activityFeedId}"/>															
<script>
				   		function showHideFormData${activityFeedId}(){
				   			if ($('[name="formContentTable${activityFeedId}"]').is(':hidden')) {
				   				$('[name="formContentTable${activityFeedId}"]').show();
						   		$('[name="formContentTd${activityFeedId}"]').css("white-space","normal");
						   		$("#formContentMore${activityFeedId}").html("See Less..");
				   		    } else {
				   		    	$('[name="formContentTable${activityFeedId}"]').hide();
						   		$('[name="formContentTd${activityFeedId}"]').css("white-space","nowrap");
						   		$("#formContentMore${activityFeedId}").html("See More..");
				   		    }
					   	}

					   	function showSubForm${activityFeedId}(fieldName){
						   		$("#subForm${activityFeedId}"+fieldName).toggle();
						   	}
				   </script>
