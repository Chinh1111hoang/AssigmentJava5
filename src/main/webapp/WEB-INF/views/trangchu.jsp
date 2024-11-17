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
    <c:if test="${not empty message}">
        <div class="alert alert-info" role="alert">
                ${message}
        </div>
    </c:if>
    <div class="row">


        <!-- Bảng danh sách hóa đơn -->
        <div class="col-md-7">
            <h3>Danh sách hóa đơn</h3>

            <table class="table" style="background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Mã HD</th>
                    <th>Tên khách</th>
                    <th>Ngày tạo</th>
                    <th>Trạng thái</th>
                    <th>Chi tiết</th>
                </tr>
                </thead>
                <tbody >
                <c:forEach items="${listHD}" var="lhd" varStatus="s">
                    <tr>
                        <td>${s.count}</td>
                        <td>${lhd.id}</td>
                        <td>${lhd.khachHang.hoTen}</td>
                        <td>${lhd.ngayTao}</td>
                        <td>${lhd.trangThai}</td>
                        <td>
                            <a href="/trangchu/xemhoadon/${lhd.id}"
                               class="btn btn-outline-primary btn-sm">Xem</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>

            </table>


            <h4>Giỏ hàng</h4>
            <table class="table" style="background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Mã Sản Phẩm</th>
                    <th>Tên Sản Phẩm</th>
                    <th>Giá Bán</th>
                    <th>Số lượng mua</th>
                    <th>Tổng tiền</th>
                    <th>Xóa</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${listHDCT}" var="lhdct" varStatus="s">
                    <tr>
                        <td>${s.count}</td>
                        <td>${lhdct.ctsp.sanPham.maSanPham}</td>
                        <td>${lhdct.ctsp.sanPham.tenSanPham}</td>
                        <td>${lhdct.giaBan}</td>
                        <td>${lhdct.soLuongMua}</td>
                        <td>${lhdct.tongTien}</td>
                        <td>
                            <a href="/trangchu/xoasanpham/${lhdct.id}"
                               class="btn btn-danger btn-sm">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>


        <div class="col-md-5">
            <h3>Tạo hóa đơn</h3>

            <form action="/trangchu/timkhachhang" method="GET" class="mb-3">
                <div class="mb-3">
                    <label for="sdt" class="form-label">Số điện thoại</label>
                    <input type="text" class="form-control" id="sdt" name="sdt" value="${sessionScope.khachHang.sdt}">
                </div>
                <button type="submit" class="btn btn-outline-primary w-100" >Tìm kiếm</button>
            </form>

            <!-- Form tạo hóa đơn -->
            <a href="/trangchu/taohoadon?sdt=${sessionScope.khachHang.sdt}" class="btn btn-outline-primary mt-1 w-100">Tạo hóa đơn mới</a>



            <!-- Form tạo hóa đơn -->
            <form action="/trangchu/thanhtoan" method="post" class="mt-2">
                <div class="mb-3">
                    <label for="tenKH" class="form-label">Tên khách hàng</label>
                    <input type="text" class="form-control" id="tenKH" name="hoTen" value="${sessionScope.hoaDon.khachHang.hoTen}" readonly>
                </div>

                <div class="mb-3">
                    <label for="maHD" class="form-label">Mã Hóa Đơn</label>
                    <input type="text" class="form-control" id="maHD" name="id" value="${sessionScope.hoaDon.id}" readonly>
                </div>

                <div class="mb-3">
                    <label for="ngayTao" class="form-label">Ngày Tạo</label>
                    <input type="text" class="form-control" id="ngayTao" name="ngayTao" value="${sessionScope.hoaDon.ngayTao}" readonly>
                </div>

                <div class="mb-3">
                    <label for="tongTien" class="form-label">Tổng Tiền</label>
                    <input type="text" class="form-control" id="tongTien" name="tongTien" value="${sessionScope.tongTien}" readonly>
                </div>

                <button type="submit" style="background-color: aqua; color: black"
                        class="btn w-100" ${empty sessionScope.hoaDon.id or sessionScope.tongTien == 0 ? 'disabled' : ''}>
                    Thanh toán
                </button>
            </form>

        </div>


        <div class="row mt-4">
            <div class="col-md-12">
                <h4>Sản Phẩm</h4>
                <table class="table" style="background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                    <thead>
                    <tr>

                        <th>STT</th>
                        <th>Mã SP</th>
                        <th>Tên SP</th>
                        <th>Màu Sắc</th>
                        <th>Size</th>
                        <th>Giá SP</th>
                        <th>Số lượng</th>
                        <th>Chọn</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${listCTSP}" var="lctsp" varStatus="s">
                    <tr>
                        <td>${s.count}</td>
                        <td>${lctsp.sanPham.maSanPham}</td>
                        <td>${lctsp.sanPham.tenSanPham}</td>
                        <td>${lctsp.mauSac.tenMau}</td>
                        <td>${lctsp.size.tenSize}</td>
                        <td>${lctsp.giaBan}</td>
                        <td>${lctsp.soLuongTon}</td>
                        <td>
                            <c:if test="${not empty sessionScope.hoaDon}">
                                <!-- Form để nhập số lượng và thêm sản phẩm vào giỏ -->
                                <form action="/trangchu/themvaogio/${lctsp.id}?idhd=${sessionScope.hoaDon.id}" method="POST" class="form-inline">
                                    <input type="number" name="soLuongMua" min="1" max="${lctsp.soLuongTon}" value="1" class="form-control form-control-sm d-inline-block" style="width: 60px;" required>
                                    <button type="submit" class="btn btn-success btn-sm">Chọn</button>
                                </form>
                            </c:if>
                            <c:if test="${empty sessionScope.hoaDon}">
                                <button class="btn btn-secondary btn-sm" disabled>Chọn</button>
                            </c:if>
                        </td>
                    </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>