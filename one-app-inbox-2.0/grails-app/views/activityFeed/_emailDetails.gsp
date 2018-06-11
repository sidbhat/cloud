<%@ page import="org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events" %>
<%@ page import="com.oneapp.cloud.core.*" %>
					<%
						def emailDetail = EmailDetails.get(shareId)
					
					 %>
					<!-- Add details for the object..Move this to a taglib-->
					<table  width='650' style="border:none;"> 
                    	<tr class="oddBackground">
                    		<td class="tableBorder tableMessagelabel" width="10%" ><b>To</b></td>
                    		<td style="white-space:normal;" width="90%" class="tableMessageContent"> ${emailDetail.emailTo.substring(1,emailDetail.emailTo?.length()-1).encodeAsHTML()} </td>
                    	</tr>
						<tr>
                    		<td  class="tableBorder tableMessagelabel"><b>From</b></td>
                    		<td style="white-space:normal;" class="tableMessageContent">${emailDetail.emailFrom.encodeAsHTML()} </td>
                    	</tr>
                    	<tr class="oddBackground">
                    		<td class="tableBorder tableMessagelabel"><b>Subject</b></td>
                    		<td class="tableMessageContent" style="white-space:normal;">${emailDetail.subject} </td>
                    	</tr>
                    	<tr id="emailDateTr${activityFeedId}" style="display:none;">
                    		<td class="tableBorder tableMessagelabel"><b>Date</b></td>
                    		<td style="white-space:normal;" class="tableMessageContent"><prettytime:display date="${emailDetail.messageTime}" /> </td>
                    	</tr>
                    	<%--<tr id="messageTr${activityFeedId}">
                    		<td><b>Message</b></td>
                    		<td>${emailDetail.content} </td>
                    	</tr>
                    	--%><%
						def attchImg
						if(emailDetail.attachmentName){
						def ext = emailDetail?.attachmentName?.substring(emailDetail.attachmentName?.lastIndexOf(".")+1,emailDetail.attachmentName?.length())
					    attchImg = '<img width="16" height="16" src="' + g.resource(plugin:'attachmentable', dir: 'images/silk', file: com.macrobit.grails.plugins.attachmentable.taglibs.AttachmentsTagLib.FILE_ICON_MAP[ext.toLowerCase()] ?: 'page_white') + '.png" />'
						%>
						<tr>
                    		<td style="background:none;" colspan=2>
                    			<a href="${request.getContextPath()}/activityFeed/getAttachFromEmail?emailAccount=${emailDetail.ruleAccount}&msgNo=${emailDetail.messageNumber}" target="_blank">${attchImg+" "+emailDetail?.attachmentName}</a>
                    		</td>
                    	</tr>
						<%
						}
						def emailContent = URLDecoder.decode(emailDetail.content,"UTF-8"); 
						%>
                    	<tr>
                    		<td colspan=2 id="emailContentTd${activityFeedId}" style="display:none;background: #fff;">
                    			<div style="overflow-x:auto;overflow-y: hidden;width:650px;" id="myEmailContent${activityFeedId}"></div> 
                    		</td>
                    	</tr>
                    	<tr>
                    		<td style="background:none;" colspan=2>
                    			<input type="button" value="Show Message" class="gray-button" onclick="showHideEmailData${activityFeedId}()" id="emailContentMore${activityFeedId}" style="cursor: pointer;">
                    		</td>
                    	</tr>
                    	
				    </table> 
				    
				   <script>
				   		function showHideEmailData${activityFeedId}(){
				   			if ($('#emailContentTd${activityFeedId}').is(':hidden')) {
				   				$('#emailContentTd${activityFeedId}').show();
				   				$('#emailDateTr${activityFeedId}').show();
				   				$('#messageTr${activityFeedId}').hide()
						   		$("#emailContentMore${activityFeedId}").val("Hide Message");
				   		    } else {
				   		    	$('#emailContentTd${activityFeedId}').hide();
				   		    	$('#emailDateTr${activityFeedId}').hide();
				   		    	$('#messageTr${activityFeedId}').show()
						   		$("#emailContentMore${activityFeedId}").val("Show Message");
				   		    }
					   	}

					   	$("#myEmailContent${activityFeedId}").html('${emailContent.encodeAsJavaScript()}')
				   </script> 
				    