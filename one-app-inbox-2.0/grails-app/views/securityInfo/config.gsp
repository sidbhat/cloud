<head>
    <meta name='layout' content='springSecurityUI'/>
    <title>Security Configuration</title>

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

<g:javascript>
        $(document).ready(function () {
            $('#config').dataTable();
        });
    </g:javascript>

</head>

<body>

<div id="configHolder">
    <table id="config" cellpadding="0" cellspacing="0" border="0" class="display">
        <thead>
        <tr>
            <th>Name</th>
            <th>Value</th>
        </tr>
        </thead>
        <tbody>
        <g:each var='entry' in='${conf}'>
            <tr>
                <td>${entry.key}</td>
                <td>${entry.value}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>

</body>

