import java.util.Date;

public class BenhAnVIP extends BenhAn {
    private String loaiVIP; // VIP 1, VIP II, VIP III
    private int thoiHanVIP; // Thời hạn VIP tháng

    public BenhAnVIP(String soThuTu, String maBenhAn, String tenBenhNhan,
                     Date ngayNhapVien, Date ngayRaVien, String lyDoNhapVien,
                     String loaiVIP, int thoiHanVIP) {
        super(soThuTu, maBenhAn, tenBenhNhan, ngayNhapVien, ngayRaVien, lyDoNhapVien);
        this.loaiVIP = loaiVIP;
        this.thoiHanVIP = thoiHanVIP;
    }

    public String getLoaiVIP() {
        return loaiVIP;
    }

    public int getThoiHanVIP() {
        return thoiHanVIP;
    }

    @Override
    public double tinhPhiDieuTri() {
        double phiCoBan = 500000; // Phí cơ bản cho VIP
        double heSo = 1.0;

        // Xác định hệ số theo loại VIP
        switch (loaiVIP.toUpperCase()) {
            case "VIP I":
                heSo = 3.0;
                break;
            case "VIP II":
                heSo = 2.0;
                break;
            case "VIP III":
                heSo = 1.5;
                break;
        }

        return tinhSoNgayDieuTri() * phiCoBan * heSo;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Loại: %s | Thời hạn: %d tháng | Tổng phí: %.0f VND",
                loaiVIP, thoiHanVIP, tinhPhiDieuTri());
    }
}