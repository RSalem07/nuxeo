<html>
<head>
    <title>Products></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>
<table class="table">
    <thead>
    <tr>
        <th scope="col">Name</th>
        <th scope="col">Title</th>
        <th scope="col">Compute Price</th>
    </tr>
    </thead>
    <tbody>
    <#list products as product>
        <tr>
            <td>${product.product_common.name}</td>
            <td>${product.dublincore.title}</td>
            <td><a href="${contextPath}/site/product/${product.id}/price">Price</a></td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>