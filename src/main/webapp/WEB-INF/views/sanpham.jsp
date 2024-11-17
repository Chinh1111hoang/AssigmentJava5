<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        .sidebar {
            height: 100vh;
            background-color: #f8f9fa;
        }

        .main-content {
            height: 100vh;
            overflow-y: auto;
        }

        .dashboard {
            background-color: bisque;
        }
    </style>
</head>
<body class="container" style="background-color: #ceffff">
<div>
    <nav class="navbar navbar-expand-lg">
        <div class="container-fluid">

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarScroll" aria-controls="navbarScroll" aria-expanded="false"
                    aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarScroll">
                <ul class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll"
                    style="--bs-scroll-height: 100px;">
                    <li class="nav-item">
                        <a class="nav-link" href="/trangchu"><i
                                class="fa-solid fa-house"></i>Bán Hàng</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false" style=" margin-left: 0px;">
                            <i class="fa-regular fa-face-smile-wink"></i> Danh Mục
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="/danhmuc">Danh mục sản phẩm</a></li>
                            <li><a class="dropdown-item" href="/hoadon">Hoá đơn</a></li>
                            <li><a class="dropdown-item" href="/sanpham">Sản phẩm</a></li>
                            <li><a class="dropdown-item" href="/ctsp">Chi tiết sản phẩm</a></li>
                            <li><a class="dropdown-item" href="/khachhang">Khách hàng</a></li>
                            <li><a class="dropdown-item" href="/mausac">Màu sắc</a></li>
                            <li><a class="dropdown-item" href="/size">Kích Thước</a></li>

                        </ul>
                    </li>
                    <div class="vertical-line"></div>

                </ul>
            </div>
        </div>
    </nav>

</div>

<form  action="/sanpham/add" method="post" style="width: 80%; height: auto; padding-left: 15%">
    <input type="hidden" name="id" value="${spdetail.id}">
    <div class="mb-3">
        <label class="form-label">Mã</label>
        <input type="text" class="form-control" name="maSanPham" value="${spdetail.maSanPham}" required>
    </div>
    <div class="mb-3">
        <label class="form-label">Tên</label>
        <input type="text" class="form-control" name="tenSanPham" value="${spdetail.tenSanPham}" required>
    </div>
    <div class="mb-3">
        <label class="form-label">Trạng thái:</label>
        <div class="form-check">
            <input class="form-check-input" type="radio" name="trangThai" value="Active" ${spdetail.trangThai == 'Active' ? 'checked' : ''}>
            <label class="form-check-label">Active</label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="radio" name="trangThai" value="UnActive" ${spdetail.trangThai == 'UnActive' ? 'checked' : ''}>
            <label class="form-check-label">UnActive</label>
        </div>
    </div>

    <div class="mb-3">
        <label for="productCategory" class="form-label">Danh mục</label>
        <select class="form-select" id="productCategory" name="danhMuc" required>
            <option value="">Chọn danh mục</option>
            <c:forEach items="${listDanhMuc}" var="dm">
                <option id="danhMuc"
                        value="${dm.id}" ${dm.id == spdetail.danhMuc.id? "selected":""}>${dm.tenDanhMuc}</option>
            </c:forEach>
        </select>
    </div>
    <div class="text-center">
        <button type="submit" class="btn btn-primary">Lưu sản phẩm</button>
    </div>
</form>
<div class="row mt-5">
    <h2 class="m-2">Sản phẩm</h2>
    <table class="table" style="background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); height: 400px;">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Danh mục sản phẩm</th>
            <th scope="col">Mã sản phẩm</th>
            <th scope="col">Tên sản phẩm</th>
            <th scope="col">Trạng thái</th>
            <th scope="col">Ngày tạo</th>
            <th scope="col">Ngày sửa</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listSP}" var="sp">
            <tr>
                <th scope="row">${sp.id}</th>
                <td>${sp.danhMuc.tenDanhMuc}</td>
                <td>${sp.maSanPham}</td>
                <td>${sp.tenSanPham}</td>
                <td>${sp.trangThai}</td>
                <td>${sp.ngayTao}</td>
                <td>${sp.ngaySua}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/sanpham/edit/${sp.id}" class="btn btn-primary btn-sm">Sửa</a>
                    <a href="${pageContext.request.contextPath}/sanpham/delete/${sp.id}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>