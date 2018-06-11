<head>
    <meta name='layout' content='springSecurityUI'/>
    <title>%{----------------------------------------------------------------------------
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

<g:message code='spring.security.ui.registrationCode.search'/></title>
</head>

<body>

<div>

    <s2ui:form width='100%' height='200' elementId='formContainer'
               titleCode='spring.security.ui.registrationCode.search'>

        <g:form action='registrationCodeSearch' name='registrationCodeSearchForm'>

            <br/>

            <table>
                <tbody>
                <tr>
                    <td><g:message code='registrationCode.username.label' default='Username'/>:</td>
                    <td><g:textField name='username' size='50' maxlength='255' autocomplete='off'
                                     value='${username}'/></td>
                </tr>
                <tr>
                    <td><g:message code='registrationCode.token.label' default='Token'/>:</td>
                    <td><g:textField name='token' size='50' maxlength='255' autocomplete='off' value='${token}'/></td>
                </tr>
                <tr><td colspan='2'>&nbsp;</td></tr>
                <tr>
                    <td colspan='2'><s2ui:submitButton elementId='search' form='registrationCodeSearchForm'
                                                       messageCode='spring.security.ui.search'/></td>
                </tr>
                </tbody>
            </table>
        </g:form>

    </s2ui:form>

    <g:if test='${searched}'>

        <%
            def queryParams = [username: username, token: token]
        %>

        <div class="list">
            <table>
                <thead>
                <tr>
                    <g:sortableColumn property="token"
                                      title="${message(code: 'registrationCode.token.label', default: 'Token')}"
                                      params="${queryParams}"/>
                    <g:sortableColumn property="user.username"
                                      title="${message(code: 'registrationCode.username.label', default: 'Username')}"
                                      params="${queryParams}"/>
                    <g:sortableColumn property="dateCreated"
                                      title="${message(code: 'registrationCode.dateCreated.label', default: 'Date Created')}"
                                      params="${queryParams}"/>
                </tr>
                </thead>

                <tbody>
                <g:each in="${results}" status="i" var="registrationCode">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        <td><g:link action="edit"
                                    id="${registrationCode.id}">${fieldValue(bean: registrationCode, field: "token")}</g:link></td>
                        <td><g:link controller='user' action='edit'
                                    params='[username: registrationCode.username]'>${fieldValue(bean: registrationCode, field: "username")}</g:link></td>
                        <td><g:formatDate formatName="format.date" date="${registrationCode.dateCreated}"/></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>

        <div class="paginateButtons">
            <g:paginate total="${totalCount}" params="${queryParams}"/>
        </div>

        <div style="text-align:center">
            <s2ui:paginationSummary total="${totalCount}"/>
        </div>

    </g:if>

</div>

<script>
    $(document).ready(function() {
        $("#username").focus().autocomplete({
            minLength: 3,
            cache: false,
            source: "${createLink(action: 'ajaxRegistrationCodeSearch')}"
        });
    });
</script>

</body>