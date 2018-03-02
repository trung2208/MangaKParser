<%-- 
    Document   : parser
    Created on : Jan 29, 2018, 10:56:16 AM
    Author     : trung
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Managk Parserfgff</title>
        <script
            src="http://code.jquery.com/jquery-3.3.1.js"
            integrity="sha256-2Kok7MbOyxpgUVvAk/HJ2jigOSYS2auK4Pfzbm7uH60="
        crossorigin="anonymous"></script>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <body>
        <%--<s:url id="fileDownload" namespace="/" action="downloadChapters" ></s:url>--%>
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Modal Header</h4>
                    </div>
                    <div class="modal-body">
                        <img src="/jsp/ajax-loader.gif" alt=""/>
                      
                    </div>
                    <div class="modal-footer">
                       
                    </div>
                </div>

            </div>
        </div>
        <h1>Managk Parser utility</h1>
        
        <s:textfield id="link" label="Link:"></s:textfield>

        <button type="button" class="btn btn-info btn-lg" data-toggle="modal" onclick="genrateLink()" data-target="#myModal">Generate</button>
        <h2 id="down"><s:a href="%{fileDownload}">click to Download</s:a>
        
            <script type="text/javascript">
            function genrateLink(){
                debugger;
                var link= $("#link").val();
                $.ajax({
                    async:false,
                    method:"POST",
                    url:"downloadChapters",
                    data:"urlChapterBean= "+link
                });
            }
            $('#wait').ajaxStart(function() {
                         $(this).show();
                }).ajaxComplete(function() {
                      $(this).hide();
                });
            
            
        </script>
    </body>


</html>
