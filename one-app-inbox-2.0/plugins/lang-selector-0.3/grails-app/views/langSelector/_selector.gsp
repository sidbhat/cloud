<%
	
%>
<div id="lang_selector" class="lang_selector">
	<g:each in="${flags.keySet().sort() }" var="lang">
		<a href="${ uri + lang }" title="Change language." class="lang_link">
			<span class="lang_flag ${ lang==selected.toString()? selected_class : not_selected_class }">
				<img alt="" src="${resource(plugin:'langSelector',dir:'images/flags/png',file:flags[lang]+'.png') }" border="0">
			</span>
		</a>
	</g:each>
</div>