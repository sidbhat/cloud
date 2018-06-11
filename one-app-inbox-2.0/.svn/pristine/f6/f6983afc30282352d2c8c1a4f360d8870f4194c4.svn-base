<head>
    <meta name='layout' content='springSecurityUI'/>
    <title>Mappings</title>
</head>

<body>

<table>
    <thead>
    <tr>
        <th>URL Pattern</th>
        <th>Filters</th>
    </tr>
    </thead>
    <tbody>
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

<g:each var='entry' in='${filterChainMap}'>
        <tr>
            <td>${entry.key}</td>
            <td>
                <g:each var='filter' in='${entry.value}'>
                    ${filter.getClass().name}<br/>
                </g:each>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>
</body>
