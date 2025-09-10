import java.util.Date;

public class BenhAnThuong extends BenhAn {
    private double phiNamVien; // Phí nằm viện (VND/ngày)

    public BenhAnThuong(String soThuTu, String maBenhAn, String tenBenhNhan,
                        Date ngayNhapVien, Date ngayRaVien, String lyDoNhapVien,
                        double phiNamVien) {
        super(soThuTu, maBenhAn, tenBenhNhan, ngayNhapVien, ngayRaVien, lyDoNhapVien);
        this.phiNamVien = phiNamVien;
    }

    public double getPhiNamVien() {
        return phiNamVien;
    }

    @Override
    public double tinhPhiDieuTri() {
        return tinhSoNgayDieuTri() * phiNamVien;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Loại: THƯỜNG | Phí nằm viện: %.0f VND/ngày | Tổng phí: %.0f VND",
                phiNamVien, tinhPhiDieuTri());
    }
}
