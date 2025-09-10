import java.text.SimpleDateFormat;
import java.util.Date;


abstract class BenhAn {
    protected String soThuTu;
    protected String maBenhAn;
    protected String tenBenhNhan;
    protected Date ngayNhapVien;
    protected Date ngayRaVien;
    protected String lyDoNhapVien;

    // Constructor
    public BenhAn(String soThuTu, String maBenhAn, String tenBenhNhan,
                  Date ngayNhapVien, Date ngayRaVien, String lyDoNhapVien) {
        this.soThuTu = soThuTu;
        this.maBenhAn = maBenhAn;
        this.tenBenhNhan = tenBenhNhan;
        this.ngayNhapVien = ngayNhapVien;
        this.ngayRaVien = ngayRaVien;
        this.lyDoNhapVien = lyDoNhapVien;
    }

    // Getter methods
    public String getSoThuTu() { return soThuTu; }
    public String getMaBenhAn() { return maBenhAn; }
    public String getTenBenhNhan() { return tenBenhNhan; }
    public Date getNgayNhapVien() { return ngayNhapVien; }
    public Date getNgayRaVien() { return ngayRaVien; }
    public String getLyDoNhapVien() { return lyDoNhapVien; }

    // Abstract method - phải được override ở lớp con
    public abstract double tinhPhiDieuTri();

    // Phương thức tính số ngày điều trị
    public int tinhSoNgayDieuTri() {
        long diff = ngayRaVien.getTime() - ngayNhapVien.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    // Override toString để hiển thị thông tin
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("STT: %s | Mã BA: %s | Tên BN: %s | Nhập viện: %s | Ra viện: %s | Lý do: %s",
                soThuTu, maBenhAn, tenBenhNhan,
                sdf.format(ngayNhapVien), sdf.format(ngayRaVien), lyDoNhapVien);
    }
}
