<ul class="listing list-view">

  <g:each in="${(taskInstance.comments.toList().sort{it.dateCreated}.reverse())}" var="comment">

    <li class="note" style="width:500px;left:45px" >

      <span class="timestamp">
        <prettytime:display date="${comment.dateCreated}"/> &nbsp;&nbsp;<g:if test="${(comment.poster.username).equals(session.user.username)}">
          <a href="#" onclick="deleteComment('${comment.id}','${taskInstance.id}')" style="color:blue;">Delete</a>
        </g:if>

      </span>

      <p>${comment.body}</p>
      <div class="entry-meta">
        Posted by ${comment.poster}
      </div>

    </li>

  </g:each>
</ul>
