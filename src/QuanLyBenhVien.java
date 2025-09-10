import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class QuanLyBenhVien {
    private List<BenhAn> danhSachBenhAn;
    private Scanner scanner;
    private SimpleDateFormat dateFormat;
    private static final String CSV_FILE = "data/benhan.csv"; // File CSV để lưu dữ liệu

    public QuanLyBenhVien() {
        danhSachBenhAn = new ArrayList<>();
        scanner = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        docDuLieuTuCSV();
        if (danhSachBenhAn.isEmpty()) {
            khoiTaoDuLieuMau();
        }
    }

    // TẠO MÃ BỆNH ÁN ĐỊNH DẠNG BA-XXX
    private String taoMaBenhAn() {
        int maxSo = 0;
        // Tìm số lớn nhất trong các mã hiện có
        for (BenhAn ba : danhSachBenhAn) {
            String ma = ba.getMaBenhAn();
            if (ma.startsWith("BA-")) {
                try {
                    int so = Integer.parseInt(ma.substring(3));
                    if (so > maxSo) {
                        maxSo = so;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu không parse được
                }
            }
        }
        // Trả về mã  BA-XXX
        return String.format("BA-%03d", maxSo + 1);
    }

    // ĐỌC DỮ LIỆU TỪ CSV
    private void docDuLieuTuCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Bỏ qua dòng header
                }

                String[] data = line.split(",");
                if (data.length >= 7) {
                    try {
                        String soThuTu = data[0];
                        String maBenhAn = data[1];
                        String tenBenhNhan = data[2];
                        Date ngayNhapVien = dateFormat.parse(data[3]);
                        Date ngayRaVien = dateFormat.parse(data[4]);
                        String lyDoNhapVien = data[5];
                        String loaiType = data[6];

                        BenhAn benhAn = null;
                        if ("THUONG".equals(loaiType) && data.length >= 8) {
                            double phiNamVien = Double.parseDouble(data[7]);
                            benhAn = new BenhAnThuong(soThuTu, maBenhAn, tenBenhNhan,
                                    ngayNhapVien, ngayRaVien, lyDoNhapVien, phiNamVien);
                        } else if ("VIP".equals(loaiType) && data.length >= 9) {
                            String loaiVIP = data[7];
                            int thoiHanVIP = Integer.parseInt(data[8]);
                            benhAn = new BenhAnVIP(soThuTu, maBenhAn, tenBenhNhan,
                                    ngayNhapVien, ngayRaVien, lyDoNhapVien, loaiVIP, thoiHanVIP);
                        }

                        if (benhAn != null) {
                            danhSachBenhAn.add(benhAn);
                        }
                    } catch (Exception e) {
                        System.out.println("Lỗi đọc dòng: " + line);
                    }
                }
            }
            System.out.println("Đã đọc " + danhSachBenhAn.size() + " bệnh án từ file CSV.");

        } catch (FileNotFoundException e) {
            System.out.println("File CSV chưa tồn tại. Sẽ tạo mới khi lưu.");
        } catch (IOException e) {
            System.out.println("Lỗi đọc file CSV: " + e.getMessage());
        }
    }

    // GHI DỮ LIỆU VÀO CSV
    private void luuDuLieuVaoCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Ghi header
            pw.println("SoThuTu,MaBenhAn,TenBenhNhan,NgayNhapVien,NgayRaVien,LyDoNhapVien,LoaiType,ThongTin1,ThongTin2");

            // Ghi từng bệnh án
            for (BenhAn ba : danhSachBenhAn) {
                StringBuilder sb = new StringBuilder();
                sb.append(ba.getSoThuTu()).append(",");
                sb.append(ba.getMaBenhAn()).append(",");
                sb.append(ba.getTenBenhNhan()).append(",");
                sb.append(dateFormat.format(ba.getNgayNhapVien())).append(",");
                sb.append(dateFormat.format(ba.getNgayRaVien())).append(",");
                sb.append(ba.getLyDoNhapVien()).append(",");

                if (ba instanceof BenhAnThuong) {
                    BenhAnThuong bat = (BenhAnThuong) ba;
                    sb.append("THUONG").append(",");
                    sb.append(bat.getPhiNamVien()).append(",");
                    sb.append(""); // Cột trống cho thông tin VIP
                } else if (ba instanceof BenhAnVIP) {
                    BenhAnVIP bav = (BenhAnVIP) ba;
                    sb.append("VIP").append(",");
                    sb.append(bav.getLoaiVIP()).append(",");
                    sb.append(bav.getThoiHanVIP());
                }

                pw.println(sb.toString());
            }

            System.out.println("Đã lưu " + danhSachBenhAn.size() + " bệnh án vào file CSV.");

        } catch (IOException e) {
            System.out.println("Lỗi ghi file CSV: " + e.getMessage());
        }
    }

    private void khoiTaoDuLieuMau() {
        try {
            // Thêm bệnh án thường
            BenhAn ba1 = new BenhAnThuong("001", "BA001", "Nguyễn Văn A",
                    dateFormat.parse("01/01/2024"),
                    dateFormat.parse("05/01/2024"),
                    "Viêm phổi", 200000);
            // Thêm bệnh án VIP
            BenhAn ba2 = new BenhAnVIP("002", "BA002", "Nguyễn Văn B",
                    dateFormat.parse("02/01/2024"),
                    dateFormat.parse("08/01/2024"),
                    "Phẫu thuật tim", "VIP I", 12);

            danhSachBenhAn.add(ba1);
            danhSachBenhAn.add(ba2);

            // Lưu dữ liệu mẫu vào CSV
            luuDuLieuVaoCSV();
        } catch (ParseException e) {
            System.out.println("Lỗi khởi tạo dữ liệu mẫu: " + e.getMessage());
        }
    }

    public void themBenhAn() {
        try {
            System.out.println("\n=== THÊM BỆNH ÁN MỚI ===");

            System.out.print("Nhập số thứ tự: ");
            String soThuTu = scanner.nextLine();

            System.out.print("Nhập mã bệnh án: ");
            String maBenhAn = scanner.nextLine();

            System.out.print("Nhập tên bệnh nhân: ");
            String tenBenhNhan = scanner.nextLine();

            System.out.print("Nhập ngày nhập viện (dd/MM/yyyy): ");
            Date ngayNhapVien = dateFormat.parse(scanner.nextLine());

            System.out.print("Nhập ngày ra viện (dd/MM/yyyy): ");
            Date ngayRaVien = dateFormat.parse(scanner.nextLine());

            System.out.print("Nhập lý do nhập viện: ");
            String lyDoNhapVien = scanner.nextLine();

            System.out.print("Chọn loại bệnh án (1-Thường, 2-VIP): ");
            int loai = Integer.parseInt(scanner.nextLine());

            BenhAn benhAn = null;
            if (loai == 1) {
                System.out.print("Nhập phí nằm viện (VND/ngày): ");
                double phiNamVien = Double.parseDouble(scanner.nextLine());

                benhAn = new BenhAnThuong(soThuTu, maBenhAn, tenBenhNhan,
                        ngayNhapVien, ngayRaVien, lyDoNhapVien, phiNamVien);
            } else if (loai == 2) {
                System.out.print("Nhập loại VIP (VIP I/VIP II/VIP III): ");
                String loaiVIP = scanner.nextLine();

                System.out.print("Nhập thời hạn VIP (tháng): ");
                int thoiHanVIP = Integer.parseInt(scanner.nextLine());

                benhAn = new BenhAnVIP(soThuTu, maBenhAn, tenBenhNhan,
                        ngayNhapVien, ngayRaVien, lyDoNhapVien, loaiVIP, thoiHanVIP);
            }
            if (benhAn != null) {
                danhSachBenhAn.add(benhAn);

                // Tự động lưu vào CSV sau khi thêm
                luuDuLieuVaoCSV();

                System.out.println("Thêm bệnh án thành công!");
            }

        } catch (Exception e) {
            System.out.println("Lỗi nhập dữ liệu: " + e.getMessage());
        }
    }

    // Hiển thị danh sách bệnh án
    public void hienThiDanhSach() {
        System.out.println("\n=== DANH SÁCH BỆNH ÁN ===");
        if (danhSachBenhAn.isEmpty()) {
            System.out.println("Danh sách trống!");
            return;
        }

        for (int i = 0; i < danhSachBenhAn.size(); i++) {
            System.out.println((i + 1) + ". " + danhSachBenhAn.get(i));
        }
    }


    private String repeat(String str, int count) {
        return String.join("", Collections.nCopies(count, str));
    }

    // Menu
    public void menu() {
        int luaChon;
        do {
            System.out.println("\n" + repeat("=", 50));
            System.out.println("   HỆ THỐNG QUẢN LÝ BỆNH VIỆN");
            System.out.println(repeat("=", 50));
            System.out.println("======MENU CHỨC NĂNG====== ");
            System.out.println(" 1. Thêm bệnh án mới");
            System.out.println(" 2. Xem danh sách bệnh án");
            System.out.println(" 3. Xóa bệnh án");
            System.out.println(" 4. Lưu dữ liệu vào CSV ");
            System.out.println(" 5. Đọc dữ liệu từ CSV )");
            System.out.println(" 6. Thoát chương trình ");
            System.out.println(" ");
            System.out.print("👉 Chọn chức năng (1-4): ");

            try {
                luaChon = Integer.parseInt(scanner.nextLine());

                switch (luaChon) {
                    case 1:
                        themBenhAn();
                        break;
                    case 2:
                        hienThiDanhSach();
                        break;
                    case 3:
                        xoaBenhAn();
                        break;
                    case 4:
                        luuDuLieuVaoCSV();
                        break;
                    case 5:
                        System.out.println("Đang đọc lại dữ liệu từ CSV...");
                        danhSachBenhAn.clear();
                        docDuLieuTuCSV();
                        break;
                    case 6:
                        System.out.println("\n" + repeat("=", 50));
                        System.out.println(" CẢM ƠN BẠN ĐÃ SỬ DỤNG HỆ THỐNG!");
                        System.out.println(" Chương trình kết thúc thành công!");
                        System.out.println(repeat("=", 50));
                        break;
                    default:
                        System.out.println(" Lựa chọn không hợp lệ! Vui lòng chọn từ 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Vui lòng nhập số từ 1-4!");
                luaChon = -1; //tiếp tục vòng lặp
            }


        } while (luaChon != 4);
    }

    // Xóa bệnh án theo mã
    public void xoaBenhAn() {
        System.out.println("\n=== XÓA BỆNH ÁN ===");
        if (danhSachBenhAn.isEmpty()) {
            System.out.println("Danh sách trống, không có gì để xóa!");
            return;
        }

        // Hiển thị danh sách hiện tại
        System.out.println("Danh sách bệnh án hiện tại:");
        for (int i = 0; i < danhSachBenhAn.size(); i++) {
            System.out.println((i + 1) + ". " + danhSachBenhAn.get(i));
        }

        System.out.print("\nNhập mã bệnh án cần xóa: ");
        String maXoa = scanner.nextLine();

        boolean daXoa = false;
        for (int i = 0; i < danhSachBenhAn.size(); i++) {
            if (danhSachBenhAn.get(i).getMaBenhAn().equalsIgnoreCase(maXoa)) {
                // Xác nhận trước khi xóa
                System.out.println("Tìm thấy bệnh án:");
                System.out.println(danhSachBenhAn.get(i));
                System.out.print("Bạn có chắc chắn muốn xóa? (y/n): ");
                String xacNhan = scanner.nextLine();

                if (xacNhan.equalsIgnoreCase("y") || xacNhan.equalsIgnoreCase("yes")) {
                    danhSachBenhAn.remove(i);

                    // Tự động lưu vào CSV sau khi xóa
                    luuDuLieuVaoCSV();

                    System.out.println("Đã xóa bệnh án thành công!");
                    daXoa = true;
                } else {
                    System.out.println("Đã hủy thao tác xóa.");
                }
                break;
            }
        }

        if (!daXoa && !danhSachBenhAn.isEmpty()) {
            System.out.println("Không tìm thấy bệnh án có mã: " + maXoa);
        }
    }
}
