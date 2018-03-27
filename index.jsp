<html>
<head>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js"></script>

    <body>

<%--TripTime: <%=session.getAttribute("ServletReceive")%>--%>
<%--String stuff = session.getAttribute("ServletReceive")--%>
<%--var yokes= <%=session.getAttribute("ServletReceive")%>;--%>
<%--Response.Write(session.getAttribute("ServletReceive"))--%>

<!-- this goes into your .jsp -->
<script>
    var receivedData = [];
    $.ajax({

        url : "ServletShowResults",
        dataType : 'json',
        error : function() {

            alert("Error Occured");
        },
        success : function(data) {


            $.each(data.jsonArray, function(index) {
                $.each(data.jsonArray[index], function(key, value) {
                    var point = [];

                    point.push(key);
                    point.push(value);
                    receivedData.push(point);

                });
            });

        }


    });
    var x = receivedData.pop();
    //out.print(x);
    console.log(x);
</script>
</body>
</head>
</html>