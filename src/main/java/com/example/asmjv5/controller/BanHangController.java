package com.example.asmjv5.controller;

import com.example.asmjv5.model.*;
import com.example.asmjv5.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BanHangController {

    @Autowired
    private HoaDonRepo hoaDonRepo;

    @Autowired
    private HdctRepo hdctRepo;

    @Autowired
    private CtspRepo ctspRepo;

    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Autowired
    private DanhMucRepo danhMucRepo;

    @Autowired
    private MauSacRepo mauSacRepo;

    @Autowired
    private SizeRepo sizeRepo;

    @Autowired
    private KhachHangRepo khachHangRepo;


    String message = "";

    SanPham sanPham = new SanPham();
    DanhMuc danhMuc = new DanhMuc();
    Ctsp ctsp = new Ctsp();
    MauSac mauSac = new MauSac();
    Size size = new Size();
    KhachHang khachHang = new KhachHang();

    private double tinhTongTienGioHang(Integer hoaDonId) {
        double tongTien = 0;
        List<Hdct> listHDCT = hdctRepo.getL(hoaDonId);

        for (Hdct h : listHDCT) {
            tongTien += h.getTongTien();
        }

        return tongTien;
    }

    @GetMapping("/trangchu")
    public String hienThi(Model model) {
        model.addAttribute("listHD", hoaDonRepo.getList());
        model.addAttribute("listCTSP", ctspRepo.findAll());
        return "trangchu";
    }

    @GetMapping("/trangchu/xemhoadon/{id}")
    public String XemHoaDon(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepo.findById(id);

        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            model.addAttribute("hoaDon", hoaDon);

            session.setAttribute("hoaDon", hoaDon);

            double tongTien = tinhTongTienGioHang(hoaDon.getId());
            session.setAttribute("tongTien", tongTien);
        } else {
            model.addAttribute("hoaDon", new HoaDon());
            session.setAttribute("tongTien", 0.0);
        }

        model.addAttribute("listCTSP", ctspRepo.findAll());
        model.addAttribute("listHDCT", hdctRepo.getL(id));
        model.addAttribute("listHD", hoaDonRepo.getList());

        return "trangchu";
    }

    @PostMapping("/trangchu/themvaogio/{id}")
    public String ThemVaoGio(@PathVariable("id") Integer id,
                             @RequestParam("soLuongMua") Integer soLuongMua,
                             HttpSession session, Model model) {
        Optional<Ctsp> optionalCTSP = ctspRepo.findById(id);
        if (optionalCTSP.isPresent()) {
            Ctsp chiTietSanPham = optionalCTSP.get();
            HoaDon hoaDon = (HoaDon) session.getAttribute("hoaDon");

            if (hoaDon != null && chiTietSanPham.getSoLuongTon() >= soLuongMua) {
                chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - soLuongMua);
                ctspRepo.save(chiTietSanPham);

                boolean existsInCart = false;

                for (Hdct h : hdctRepo.findAll()) {
                    if (h.getCtsp().getId().equals(chiTietSanPham.getId()) && h.getHoaDon().getId().equals(hoaDon.getId())) {
                        h.setSoLuongMua(h.getSoLuongMua() + soLuongMua);
                        h.setTongTien(h.getSoLuongMua() * h.getGiaBan());
                        hdctRepo.save(h);
                        existsInCart = true;
                        break;
                    }
                }

                if (!existsInCart) {
                    Hdct hoaDonChiTiet = new Hdct(null, hoaDon, chiTietSanPham, soLuongMua, chiTietSanPham.getGiaBan(), soLuongMua * chiTietSanPham.getGiaBan(), "Da thanh toan", java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now()));
                    hdctRepo.save(hoaDonChiTiet);
                }

                double tongTien = tinhTongTienGioHang(hoaDon.getId());
                session.setAttribute("tongTien", tongTien);
                session.setAttribute("listHDCT", hdctRepo.getL(hoaDon.getId()));
            }
        }

        return "redirect:/trangchu";
    }

    @GetMapping("/trangchu/taohoadon")
    public String TaoHoaDon(String soDT, HttpSession session) {
        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

        if (khachHang != null) {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setKhachHang(khachHang);
            hoaDon.setTrangThai("Chua thanh toan");
            hoaDon.setNgayTao(new java.util.Date());
            hoaDon.setNgaySua(new java.util.Date());
            hoaDon.setSoDienThoai(khachHang.getSdt());
            hoaDon.setDiaChi(khachHang.getDiaChi());

            hoaDonRepo.save(hoaDon);
            session.setAttribute("hoaDon", hoaDon);
            session.setAttribute("tongTien", 0.0);

        }
        return "redirect:/trangchu";
    }
    @PostMapping("/trangchu/thanhtoan")
    public String ThanhToan(@RequestParam("id") Integer hoaDonId, HttpSession session, Model model) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepo.findById(hoaDonId);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();

            Double tongTien = (Double) session.getAttribute("tongTien");
            if (hoaDon.getId() == 0 || tongTien == null || tongTien == 0) {
                model.addAttribute("message", "Không thể thanh toán vì hóa đơn hoặc tổng tiền không hợp lệ.");
                return "redirect:/trangchu";
            }

            hoaDon.setTrangThai("Da thanh toan");

            hoaDon.setNgaySua(java.sql.Date.valueOf(LocalDate.now()));

            hoaDonRepo.save(hoaDon);

            session.removeAttribute("hoaDon");
            session.removeAttribute("tongTien");
            session.removeAttribute("listHDCT");

        }
        return "redirect:/trangchu";
    }

    @GetMapping("/trangchu/timkhachhang")
    public String TimKhachHang(@RequestParam("sdt") String sdt, Model model, HttpSession session) {
        Optional<KhachHang> khachHang = khachHangRepo.findBySdt(sdt);
        HoaDon hoaDon = new HoaDon();

        if (khachHang.isPresent()) {
            hoaDon.setKhachHang(khachHang.get());
            session.setAttribute("hoaDon", hoaDon);
            session.setAttribute("khachHang", khachHang.get());

        } else {
            session.setAttribute("hoaDon", new HoaDon());
        }

        model.addAttribute("listCTSP", ctspRepo.findAll());
        model.addAttribute("listHDCT", hdctRepo.getL(hoaDon.getId()));
        model.addAttribute("listHD", hoaDonRepo.getList());

        return "redirect:/trangchu";
    }

    @GetMapping("/trangchu/xoasanpham/{id}")
    public String XoaSanPhamGioHang(@PathVariable("id") Integer id, HttpSession session) {
        Optional<Hdct> optionalHDCT = hdctRepo.findById(id);

        if (optionalHDCT.isPresent()) {
            Hdct hoaDonChiTiet = optionalHDCT.get();
            Ctsp chiTietSanPham = hoaDonChiTiet.getCtsp();

            // Cộng lại số lượng mua vào số lượng tồn của sản phẩm
            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + hoaDonChiTiet.getSoLuongMua());
            ctspRepo.save(chiTietSanPham);

            // Xóa sản phẩm khỏi giỏ hàng
            hdctRepo.delete(hoaDonChiTiet);

            // Lấy hóa đơn từ session
            HoaDon hoaDon = (HoaDon) session.getAttribute("hoaDon");

            // Cập nhật tổng tiền giỏ hàng
            double tongTien = tinhTongTienGioHang(hoaDon.getId());
            session.setAttribute("tongTien", tongTien);

            // Cập nhật lại danh sách sản phẩm trong giỏ hàng
            session.setAttribute("listHDCT", hdctRepo.getL(hoaDon.getId()));
        }

        return "redirect:/trangchu";
    }

    @GetMapping("/hoadon")
    public String getHoaDon(Model model) {
        List<HoaDon> hoaDonList = hoaDonRepo.findAll();
        model.addAttribute("hoaDonList", hoaDonList);
        return "hoadon";
    }

    @GetMapping("/sanpham")
    public String sanpham(Model model) {
        model.addAttribute("listSP", sanPhamRepo.findAll());
        if (message.equalsIgnoreCase("updateSP")) {
            model.addAttribute("spdetail", new SanPham());
        } else {
            if (!String.valueOf(sanPham.getId()).equalsIgnoreCase("")) {
                model.addAttribute("spdetail", sanPham);
            }
        }
        model.addAttribute("listDanhMuc", danhMucRepo.findAll());
        return "sanpham";
    }
    @PostMapping("/sanpham/add")
    public String saveSanPham(@ModelAttribute SanPham sanPham) {
        if (sanPham.getId() != 0) {
            Optional<SanPham> existingSP = sanPhamRepo.findById(sanPham.getId());
            if (existingSP.isPresent()) {
                SanPham updateSP = existingSP.get();
                updateSP.setTenSanPham(sanPham.getTenSanPham());
                updateSP.setMaSanPham(sanPham.getMaSanPham());
                updateSP.setDanhMuc(sanPham.getDanhMuc());
                if (sanPham.getTrangThai().equalsIgnoreCase("Active")) {
                    updateSP.setTrangThai("Active");
                } else {
                    updateSP.setTrangThai("UnActive");
                }
                updateSP.setNgaySua(new Date());
                message = "updateSP";
                sanPhamRepo.save(updateSP);
            }
        } else {
            sanPham.setNgayTao(new Date());
            sanPham.setNgaySua(new Date());
            message = "addSP";
            sanPhamRepo.save(sanPham);
        }
        return "redirect:/sanpham";
    }
    @GetMapping("/sanpham/delete/{id}")
    public String deleteSP(@PathVariable int id) {
        sanPhamRepo.deleteById(id);
        return "redirect:/sanpham";
    }
    @GetMapping("/sanpham/edit/{id}")
    public String editSP(@PathVariable int id) {
        sanPham = sanPhamRepo.findById(id).orElse(new SanPham());
        message = "";
        return "redirect:/sanpham";
    }
    @GetMapping("/danhmuc")
    public String danhmuc(Model model) {
        model.addAttribute("listDM", danhMucRepo.findAll());
        if (message.equalsIgnoreCase("udpateDM")) {
            model.addAttribute("danhMuc", new DanhMuc());
        } else {
            if (!String.valueOf(danhMuc.getId()).equalsIgnoreCase("")) {
                model.addAttribute("danhMuc", danhMuc);
            }
        }
        model.addAttribute("danhMuc", danhMuc);
        return "danhmuc";
    }
    @PostMapping("/danhmuc/save")
    public String saveDanhMuc(@ModelAttribute DanhMuc danhMuc) {
        if (danhMuc.getId() != 0) {
            Optional<DanhMuc> existingDanhMuc = danhMucRepo.findById(danhMuc.getId());

            if (existingDanhMuc.isPresent()) {
                DanhMuc updatedDanhMuc = existingDanhMuc.get();
                updatedDanhMuc.setTenDanhMuc(danhMuc.getTenDanhMuc());
                updatedDanhMuc.setMaDanhMuc(danhMuc.getMaDanhMuc());
                updatedDanhMuc.setTrangThai(danhMuc.getTrangThai());
                updatedDanhMuc.setNgaySua(new Date());
                message = "updateDM";
                danhMucRepo.save(updatedDanhMuc);
            }
        }else {
            danhMuc.setNgayTao(new Date());
            danhMuc.setNgaySua(new Date());
            danhMucRepo.save(danhMuc);
        }
        return "redirect:/danhmuc";
    }

    @GetMapping("/danhmuc/delete/{id}")
    public String deleteDanhMuc(@PathVariable int id) {
        danhMucRepo.deleteById(id);
        return "redirect:/danhmuc";
    }

    @GetMapping("/danhmuc/edit/{id}")
    public String editDanhMuc(@PathVariable int id) {
        danhMuc = danhMucRepo.findById(id).orElse(new DanhMuc());
        message = "";
        return "danhmuc";
    }

    @GetMapping("/ctsp")
    public String getAllCTSP(Model model) {
        List<Ctsp> listCTSP = ctspRepo.findAll();
        List<SanPham> listSP = sanPhamRepo.findAll();
        List<MauSac> listMS = mauSacRepo.findAll();
        List<Size> listSize = sizeRepo.findAll();
        if (message.equalsIgnoreCase("udpatectsp")) {
            model.addAttribute("CTSP", new Ctsp());
        } else {
            if (!String.valueOf(ctsp.getId()).equalsIgnoreCase("")) {
                model.addAttribute("CTSP", ctsp);
            }
        }
        model.addAttribute("listCTSP", listCTSP);
        model.addAttribute("listSPCT", listSP);
        model.addAttribute("listSize", listSize);
        model.addAttribute("listMS", listMS);
        return "ctsp";
    }



    @PostMapping("/ctsp/save")
    public String saveCTSP(@ModelAttribute Ctsp ctsp) {
        if (ctsp.getId() != 0) {
            Optional<Ctsp> existingSP = ctspRepo.findById(ctsp.getId());
            if (existingSP.isPresent()) {
                Ctsp updateCTSP = existingSP.get();
                updateCTSP.setGiaBan(ctsp.getGiaBan());
                updateCTSP.setSoLuongTon(ctsp.getSoLuongTon());
                updateCTSP.setSanPham(ctsp.getSanPham());
                updateCTSP.setMauSac(ctsp.getMauSac());
                updateCTSP.setSize(ctsp.getSize());
                updateCTSP.setTrangThai(ctsp.getTrangThai());
                updateCTSP.setNgaySua(new Date());
                message = "updatectsp";
                ctspRepo.save(updateCTSP);

            }
        } else {
            ctsp.setNgayTao(new Date());
            ctsp.setNgaySua(new Date());
            ctspRepo.save(ctsp);
        }

        return "redirect:/ctsp";
    }

    @GetMapping("/ctsp/delete/{id}")
    public String deletectsp(@PathVariable Integer id) {
        ctspRepo.deleteById(id);
        return "redirect:/ctsp";
    }

    @GetMapping("/ctsp/edit/{id}")
    public String editSPCT(@PathVariable Integer id) {
        ctsp = ctspRepo.findById(id).orElse(new Ctsp());
        message = "";
        return "redirect:/ctsp";
    }

    @GetMapping("/khachhang")
    public String getKH(Model model) {
        List<KhachHang> list = khachHangRepo.findAll();
        model.addAttribute("listKH", list);
        if (message.equalsIgnoreCase("updatekh")) {
            model.addAttribute("khachHang", new KhachHang());
        } else {
            if (!String.valueOf(khachHang.getId()).equalsIgnoreCase("")) {
                model.addAttribute("khachHang", khachHang);
            }
        }
        return "khachhang";
    }

    @PostMapping("/khachhang/save")
    public String saveKH(@ModelAttribute KhachHang khachHang) {
        Optional<KhachHang> existingDanhMuc = khachHangRepo.findById(khachHang.getId());

        if (existingDanhMuc.isPresent()) {
            KhachHang updatedDanhMuc = existingDanhMuc.get();
            updatedDanhMuc.setHoTen(khachHang.getHoTen());
            updatedDanhMuc.setDiaChi(khachHang.getDiaChi());
            updatedDanhMuc.setSdt(khachHang.getSdt());
            updatedDanhMuc.setTrangThai(khachHang.getTrangThai());
            updatedDanhMuc.setNgaySua(new Date());
            message = "updatekh";
            khachHangRepo.save(updatedDanhMuc);
        } else {
            khachHang.setNgayTao(new Date());
            khachHang.setNgaySua(new Date());
            khachHangRepo.save(khachHang);
        }

        return "redirect:/khachhang";
    }

    @GetMapping("/khachhang/delete/{id}")
    public String deleteKH(@PathVariable Integer id) {
        khachHangRepo.deleteById(id);
        return "redirect:/khachhang";
    }

    @GetMapping("/khachhang/edit/{id}")
    public String editKH(@PathVariable Integer id) {
        khachHang = khachHangRepo.findById(id).orElse(new KhachHang());
        message = "";
        return "redirect:/khachhang";
    }

    @GetMapping("/mausac")
    public String getMauSac(Model model) {
        List<MauSac> list = mauSacRepo.findAll();
        model.addAttribute("listMS", list);
        if (message.equalsIgnoreCase("updatems")) {
            model.addAttribute("mauSac", new MauSac());
        } else {
            if (!String.valueOf(mauSac.getId()).equalsIgnoreCase("")) {
                model.addAttribute("mauSac", mauSac);
            }
        }
        return "mausac";
    }

    @PostMapping("/mausac/save")
    public String saveMauSac(@ModelAttribute MauSac mauSac) {
        Optional<MauSac> existingDanhMuc = mauSacRepo.findById(mauSac.getId());

        if (existingDanhMuc.isPresent()) {
            MauSac updatedDanhMuc = existingDanhMuc.get();
            updatedDanhMuc.setTenMau(mauSac.getTenMau());
            updatedDanhMuc.setMaMau(mauSac.getMaMau());
            updatedDanhMuc.setTrangThai(mauSac.getTrangThai());
            updatedDanhMuc.setNgaySua(new Date());
            message = "updatems";
            mauSacRepo.save(updatedDanhMuc);
        } else {
            mauSac.setNgayTao(new Date());
            mauSac.setNgaySua(new Date());
            mauSacRepo.save(mauSac);
        }

        return "redirect:/mausac";
    }

    @GetMapping("/mausac/delete/{id}")
    public String deleteMauSac(@PathVariable Integer id) {
        mauSacRepo.deleteById(id);
        return "redirect:/mausac";
    }

    @GetMapping("/mausac/edit/{id}")
    public String editMauSac(@PathVariable Integer id) {
        mauSac = mauSacRepo.findById(id).orElse(new MauSac());
        message = "";
        return "redirect:/mausac";
    }

    @GetMapping("/size")
    public String getAll(Model model) {
        List<Size> list = sizeRepo.findAll();
        model.addAttribute("listSize", list);
        if (message.equalsIgnoreCase("updatesize")) {
            model.addAttribute("size", new Size());
        } else {
            if (!String.valueOf(size.getId()).equalsIgnoreCase("")) {
                model.addAttribute("size", size);
            }
        }
        return "size";
    }

    @PostMapping("/size/save")
    public String saveDanhMuc(@ModelAttribute Size size) {
        Optional<Size> existingDanhMuc = sizeRepo.findById(size.getId());

        if (existingDanhMuc.isPresent()) {
            Size updatedDanhMuc = existingDanhMuc.get();
            updatedDanhMuc.setTenSize(size.getTenSize());
            updatedDanhMuc.setMaSize(size.getMaSize());
            updatedDanhMuc.setTrangThai(size.getTrangThai());
            updatedDanhMuc.setNgaySua(new Date());
            message = "updatesize";
            sizeRepo.save(updatedDanhMuc);
        } else {
            size.setNgayTao(new Date());
            size.setNgaySua(new Date());
            sizeRepo.save(size);
        }
        return "redirect:/size";
    }

    @GetMapping("/size/delete/{id}")
    public String deleteSize(@PathVariable int id) {
        sizeRepo.deleteById(id);
        return "redirect:/size";
    }

    @GetMapping("/size/edit/{id}")
    public String editSize(@PathVariable int id) {
        size = sizeRepo.findById(id).orElse(new Size());
        message = "";
        return "redirect:/size";
    }

}
