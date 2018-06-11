<html>
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    %{----------------------------------------------------------------------------
  - [ NIKKISHI CONFIDENTIAL ]                                                -
  -                                                                          -
  -    Copyright (c) 2011.  Nikkishi LLC                                     -
  -    All Rights Reserved.                                                  -
  -                                                                          -
  -   NOTICE:  All information contained herein is, and remains              -
  -   the property of Nikkishi LLC and its suppliers,                        -
  -   if any.  The intellectual and technical concepts contained             -
  -   herein are proprietary to Nikkishi LLC and its                         -
  -   suppliers and may be covered by U.S. and Foreign Patents,              -
  -   patents in process, and are protected by trade secret or copyright law.
  -   Dissemination of this information or reproduction of this material     -
  -   is strictly forbidden unless prior written permission is obtained      -
  -   from Nikkishi LLC.                                                     -
  ----------------------------------------------------------------------------}%

<g:set var="entityName" value="${message(code: 'reports.label', default: 'Report')}"/>
    <title><g:message code="default.report.label" args="[entityName]"/></title>
    <!-- 2. Add the JavaScript to initialize the chart on document ready -->
    <script type="text/javascript">

        var chart;
        $(document).ready(function() {
            chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'container',
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                title: {
                    text: '${title}'
                },
                tooltip: {
                    formatter: function() {
                        return '<b>' + this.point.name + '</b>: ' + this.y + ' %';
                    }
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: false
                        },
                        showInLegend: true
                    }
                },
                series: [
                    {
                        type: 'pie',
                        name: '${dataLabel}',
                        data: ${data}
                    }
                ]
            });
        });

    </script>

</head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
            <g:hasErrors bean="${reportInstance}">
                <div class="errors">
                    <g:renderErrors bean="${reportInstance}" as="list"/>
                </div>
            </g:hasErrors>

                <div class="view-switcher">
                <h2>${viewName} <a href="#">&darr;</a></h2>
                <ul>
        			<li><g:link controller="report" action="oppReport" params="[report:'opportunity']"><g:message
                            code="report.opportunity" default: 'Opportunities'/></g:link></li>
        			<li><g:link controller="report" action="projectReport" params="[report:'project']"><g:message
                            code="report.project" default: 'Project'/></g:link></li>
                    <li><g:link controller="report" action="invoiceReport" params="[report:'invoice']"><g:message
                            code="report.invoice" default: 'Invoice'/></g:link></li>
 		             <li><g:link controller="report" action="oppPieReport" params="[report:'projectBillable']"><g:message
                            code="report.project.billable" default: 'Billable/Non Billable'/></g:link></li>
                     <li><g:link controller="report" action="feedLineReport" params="[report:'feed']"><g:message
                            code="report.company.feed" default: 'Feed Report'/></g:link></li>
                </ul>
            </div>

        </header>
        <!-- Main Section -->

        <section class="container_6 clearfix">
            <!-- 3. Add the container -->
            <div id="container" class="grid_6"></div>
        </section>
    </div>

</section>

<!-- Main Section End -->

</div>
</section>
</body>
</html>
