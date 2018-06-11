package org.doc4web.grails.icepush

class IcepushTagLib {


  static namespace = "icep"

  private wrapContext = {attrs, Closure c ->
    def pc = org.icepush.PushContext.getInstance(servletContext)

    def group = attrs['group']
    def pushid = attrs['pushid'] ?: pc.createPushId(request, response)

    if (!group)
      group = pushid;

    pc.addGroupMember(group, pushid);
    c(pushid, group)
  }

  def bridge = {attrs, body ->
	  def contextPath = attrs.contextPath?:null
    out << """<script type="text/javascript" src="${contextPath?(contextPath+"/"):""}code.icepush"></script>
    <script type="text/javascript">var notificationIds = [];var contextPathURL='${contextPath?:"."}';</script>"""
  }

  def region = {attrs, body ->
    wrapContext(attrs) { pushid, group ->
      def id = attrs['wrapperid'] ?: pushid
      attrs.params = attrs.params ?: [:]
      attrs.params['group'] = group
      def attrsClone = attrs.clone()

      def url = g.createLink(attrs)

      def evalJS = attrs['evalJS'] ?: false

      out << "<div id=\"$id\""
      out << "${attrs.collect {k, v -> " $k=\"$v\"" }.join('')}>"
      if (!body())
        out << g.include(attrsClone)
      else
        out << body()
      out << "</div>"
      out << """<script type="text/javascript">
            if(!('$pushid' in notificationIds)){
            notificationIds.push('$pushid');
			ice.push.register(['$pushid'], function(){
			  ice.push.get('$url',
			  function(parameter) {
			  } ,
			  function(statusCode, responseText) {
			      var container = document.getElementById('$id');
	  			  if(\$(container).attr("group")=="secondGroup"){
	  					if(responseText!="0"){
	  						container.parentNode.style.display="block";
	  						container.innerHTML = responseText;
	  						setCountAndFeed()
	  					}else{
	  						if(container.innerHTML == 0 && responseText == 0)
	  							container.parentNode.style.display="none";
	  					}
	  					chatHeartbeat()
	  				}else{
	  					container.innerHTML = responseText;
	  				}
              """
      if (evalJS) {
        out << "ice.push.searchAndEvaluateScripts(container);"
      }
      out << """ });
			});
			}
			</script> """
    }

  }

  def page = {attrs, body ->
    wrapContext(attrs) { pushid, group ->
      attrs.params = attrs.params ?: [:]
      attrs.params['group'] = group
      def url = g.createLink(attrs)

      out << """<script type="text/javascript">
            if(!('$pushid' in notificationIds)){
            notificationIds.push('$pushid');
			ice.push.register(['$pushid'], function(){
			  ice.push.get('$url',
			  function(parameter) {
			  } ,
			  function(statusCode, responseText) {
			      window.location.href = "$url";
			  });
			});
			}
			</script> """
    }
  }

  def pushId = { attrs, body ->
    def pc = org.icepush.PushContext.getInstance(servletContext)
    def id = pc.createPushId(request, response)
    out << id
  }

  def register = {attrs, body ->
    wrapContext(attrs) { pushid, group ->
      out << """<script type="text/javascript">
      if(!('$pushid' in notificationIds)){
            notificationIds.push('$pushid');
			ice.push.register(['$pushid'], function(){
			  ${attrs['callback']};
			});
		}
			</script> """
    }
  }

  def push = {attrs, body ->
    def pc = org.icepush.PushContext.getInstance(servletContext)
    pc.push(attrs['group'])
  }

}
