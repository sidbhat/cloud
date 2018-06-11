
<%@ page import="com.oneapp.cloud.time.Task; com.oneapp.cloud.time.MileStone" contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Assign Task</title>
  <meta name="layout" content="main" />
    <script type="text/javascript" src="${resource(dir: "js/jquery", file: "jquery-1.5.1.js")}"></script>
    <script type="text/javascript" src="${resource(dir: "js/jquery", file: "jquery-ui-1.8.custom.min.js")}"></script>
   
  <script>
    var data = new Array();

    $().ready(function() {
      $('#save').attr('disabled', 'disabled');

      $("div[id^=milestone]").droppable({

        drop: function(event, ui) {

          assignTaskToMilestone(this.id, ui.draggable[0].id)
        },
        out:function(evnet, ui) {
          removeTaskFromMilestone(this.id, ui.draggable[0].id);
        }

      });

      $("div[id^=task]").draggable({cursor: 'hand'})
    });

    function assignTaskToMilestone(milestone, task) {
      var addedItem = milestone + "@" + task
      $.ajax({
        url: "${grailsApplication.config.grails.serverURL}/mileStone/saveAssignTask",
        dataType: 'json',
        data: {data:addedItem} ,
        success: function(data) {
          $('#message').html("Data Saved Successfully")
        }
      });
      $('#' + task).hide()
      $('#' + milestone).append("<br/><div id=" + task + " >" + document.getElementById(task).innerHTML + "</div>")
      $("div[id^=task]").draggable()
      data.push(milestone.toString() + "@" + task.toString())
      if (data.length >= 1) {
        $('#save').removeAttr('disabled');
      }

    }

    function removeTaskFromMilestone(milestone, task) {
      var removeItem = milestone + "@" + task
      $.ajax({
        url: "${grailsApplication.config.grails.serverURL}/mileStone/removeTask",
        dataType: 'json',
        data: {data:removeItem} ,
        success: function(data) {
          $('#message').html("Removed Successfully")
        }
      });

      data = jQuery.grep(data, function(value) {
        return value != removeItem;
      });
      if (data.length < 1) {
        $('#save').attr('disabled', 'disabled');
      }
    }


  </script>
</head>
<body>

<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <h2>Group Assignment</h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
            <g:hasErrors bean="${groupsInstance}">
                <div class="errors">
                    <g:renderErrors bean="${groupsInstance}" as="list"/>
                </div>
            </g:hasErrors>
            <div id="message"></div>

        </header>
        <section class="container_6 clearfix">
            <div class="form grid_6">

<div id="wrapper">
  <div class="LeftPart">
    <g:if test="${milestoneList}">
      <h1>Milestone List</h1>
    </g:if>
    <g:else>
      <h1>No Milestones Created</h1>
    </g:else>

    <g:each in="${milestoneList}" var="milestone">
      <div id="milestone-${milestone.id}" class="MatTOp">${milestone.description}
        <g:each in="${milestone.task}" var="task">
          <div id="task-${task.id}">${task.name}- Due <g:formatDate date="${task.dueDate}" formatName="format.date"/></div>
        </g:each>
      </div>

    </g:each>

  </div>

  <div class="LeftPart">
    <g:if test="${taskList}">
      <h2>Task List</h2>
      <g:each in="${taskList}" var="task" status="count">

        <g:if test="${(count%2)==0}">

          <div id="task-${task.id}">${task.name}- Due <g:formatDate format="format.date" date="${task.dueDate}"/></div>
        </g:if>
        <g:else>
          <div id="task-${task.id}" class="Altrnatv">${task.name}- Due <g:formatDate formatName="format.date" date="${task.dueDate}"/></div>
        </g:else>

      </g:each>
    </g:if><g:else>
    <h2>No Tasks To Assign</h2>
  </g:else>
  </div>
 
  </div>
</div>
  </div>
        </section>
    </div>
</section>
</body>
</html>



