<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <title>Document</title>
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

<div class="container mt-4">
    <h2 class="m-2">Danh sách hóa đơn</h2>
    <table class="table" style="background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Tên Khách Hàng</th>
            <th scope="col">Địa Chỉ</th>
            <th scope="col">Số điệm thoại</th>
            <th scope="col">Tên sản phẩm</th>
            <th scope="col">Số lượng</th>
            <th scope="col">Đơn giá</th>
            <th scope="col">Tổng tiền</th>
            <th scope="col">Trạng thái</th>
            <th scope="col">Ngày tạo</th>
            <th scope="col">Ngày sửa</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="hoaDon" items="${hoaDonList}" varStatus="s">
            <tr>
                <th scope="row">${s.count}</th>
                <td>${hoaDon.khachHang.hoTen}</td>
                <td>${hoaDon.diaChi}</td>
                <td>${hoaDon.soDienThoai}</td>
                <td>
                    <c:forEach var="hdct" items="${hoaDon.hoaDonChiTiet}">
                        ${hdct.ctsp.sanPham.tenSanPham}<br>
                    </c:forEach>
                </td>
                <td>
                    <c:forEach var="hdct" items="${hoaDon.hoaDonChiTiet}">
                        ${hdct.soLuongMua}<br>
                    </c:forEach>
                </td>
                <td>
                    <c:forEach var="hdct" items="${hoaDon.hoaDonChiTiet}">
                        ${hdct.giaBan}<br>
                    </c:forEach>
                </td>
                <td>
                    <c:forEach var="hdct" items="${hoaDon.hoaDonChiTiet}">
                        ${hdct.tongTien}<br>
                    </c:forEach>
                </td>
                <td>${hoaDon.trangThai}</td>
                <td>${hoaDon.ngayTao}</td>
                <td>${hoaDon.ngaySua}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>