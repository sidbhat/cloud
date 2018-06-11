<g:if test="${itemImageURLs }">
<style>
body{
margin:0;
}
.slideShow img{
width:100%;
border-bottom:1px dashed white;
}
.slideShow img:last-child{
border-bottom:0;
}
</style>
<%--<g:javascript src="${request.getContextPath() }/js/slide.js"></g:javascript>--%>
<div class="slideShow">
	<g:each in="${itemImageURLs }" var="imgURL">
		<img src="${imgURL }">
	</g:each>
</div>
</g:if>
<g:else>
<style>
body{
color:white;
}
</style>
No image preview is available
</g:else>