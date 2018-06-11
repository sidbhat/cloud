<%@ page import="org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events" %>
<%@ page import="com.oneapp.cloud.core.*" %>
					<%
						def emailDetail = EmailDetails.get(shareId)
						def emailContent = URLDecoder.decode(emailDetail.content,"UTF-8");
					
					 %>
					<!-- Add details for the object..Move this to a taglib-->
					<table class="datatable" width='250px' style="border:none;"> 
                    	<tr name="formContentTable${activityFeedId}">
                    		<td style="max-width:100px;width:50px;background: none;border:none;padding:2px;vertical-align:top;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" ><b>To</b></td>
                    		<td style="max-width:140px;width:140px;background: none;border:none;padding:2px;vertical-align:top;font-weight:normal;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" name="formContentTd${activityFeedId}"> ${emailDetail.emailTo.substring(1,emailDetail.emailTo?.length()-1).encodeAsHTML()} </td>
                    	</tr>
						<tr name="formContentTable${activityFeedId}">
                    		<td style="max-width:100px;width:50px;background: none;border:none;padding:2px;vertical-align:top;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" ><b>From</b></td>
                    		<td style="max-width:140px;width:140px;background: none;border:none;padding:2px;vertical-align:top;font-weight:normal;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" name="formContentTd${activityFeedId}">${emailDetail.emailFrom.encodeAsHTML()} </td>
                    	</tr>
                    	<%--<tr name="formContentTable${activityFeedId}">
                    		<td style="max-width:100px;width:70px;background: none;border:none;padding:2px;vertical-align:top;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" ><b>Date</b></td>
                    		<td style="max-width:100px;width:50px;background: none;border:none;padding:2px;vertical-align:top;font-weight:normal;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" name="formContentTd${activityFeedId}">${emailDetail.messageTime} </td>
                    	</tr>
                    	--%><tr name="formContentTable${activityFeedId}">
                    		<td style="max-width:100px;width:50px;background: none;border:none;padding:2px;vertical-align:top;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" ><b>Subject</b></td>
                    		<td style="max-width:100px;width:140px;background: none;border:none;padding:2px;vertical-align:top;font-weight:normal;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" name="formContentTd${activityFeedId}">${emailDetail.subject} </td>
                    	</tr>
                    	
                    	<tr name="formContentTable${activityFeedId}">
                    		<td colspan=2 id="emailContentTd${activityFeedId}" style="display:none;" >
                    			<div name="formContentTd${activityFeedId}" id="myEmailContent${activityFeedId}" style="font-family: Helvetica;max-width:250px;width:100%;background: none;border:none;padding:2px;vertical-align:top;white-space:nowrap;overflow-x:auto;overflow-y:hidden;text-overflow:ellipsis;font-size:12px;">
                    			</div>
                    		 </td>
                    	</tr>
                    	<tr>
                    		<td style="background:none;" colspan=2>
                    			<a href="javascript:;" id="emailContentMore${activityFeedId}">Show Content</a>
                    		</td>
                    	</tr>
                    	
				    </table> 
				    
				   <script>
				   		$("#emailContentMore${activityFeedId}").click(function(){
				   			if ($('#emailContentTd${activityFeedId}').is(':hidden')) {
				   				$('#emailContentTd${activityFeedId}').show();
				   				$('[name="formContentTd${activityFeedId}"]').css("white-space","normal");
						   		$("#emailContentMore${activityFeedId}").html("Hide Content");
				   		    } else {
				   		    	$('#emailContentTd${activityFeedId}').hide();
				   		    	$('[name="formContentTd${activityFeedId}"]').css("white-space","nowrap");
						   		$("#emailContentMore${activityFeedId}").html("Show Content");
				   		    }
					   	});

				   		$("#myEmailContent${activityFeedId}").html('${emailContent.encodeAsJavaScript()}')
				   </script> 
				    