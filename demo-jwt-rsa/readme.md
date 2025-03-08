# JWT với Khóa RSA & Redis

## 📌 Giới thiệu
Dự án này hướng dẫn cách sử dụng **JWT (JSON Web Token) với RSA** trong **Spring Boot**, cùng với **Refresh Token và Redis** để quản lý phiên đăng nhập an toàn.

---

## 🔑 **JWT là gì?**
JWT (**JSON Web Token**) là một chuẩn **token-based authentication** giúp xác thực người dùng mà **không cần lưu trạng thái (stateless)** trên server.

- **Access Token**: Dùng để xác thực request API, thời gian sống ngắn (~5-15 phút).
- **Refresh Token**: Dùng để lấy Access Token mới khi hết hạn, lưu trong **Redis**, thời gian sống dài (~7 ngày).

JWT gồm 3 phần chính:
```txt
Header.Payload.Signature
```
Ví dụ JWT:
```txt
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9... (cắt bớt)
```
---

## 🔐 **RSA là gì? Tại sao dùng RSA trong JWT?**
### **1️⃣ RSA là gì?**
RSA (**Rivest-Shamir-Adleman**) là thuật toán **mã hóa bất đối xứng**, sử dụng **Private Key** để ký và **Public Key** để xác thực.

### **2️⃣ Tại sao dùng RSA với JWT?**
✅ **Bảo mật cao** hơn so với HMAC (symmetric key).  
✅ **Có thể chia sẻ Public Key** cho nhiều dịch vụ để xác thực.

📌 **Quy trình hoạt động của JWT với RSA:**
1. Server tạo **cặp khóa RSA** (Private & Public Key).
2. Khi user đăng nhập, server dùng **Private Key để ký JWT**.
3. Client gửi JWT kèm theo request.
4. Server **dùng Public Key để xác thực JWT**.

---

## 🛠 **Cài đặt Redis Container bằng Docker**
Redis sẽ lưu **Refresh Token** để quản lý phiên đăng nhập.

### **🔹 Cài đặt Redis bằng Docker**
```sh
docker run --name redis-jwt -p 6379:6379 -d redis
```
📌 Kiểm tra Redis đã chạy chưa:
```sh
docker ps
```
📌 Mở Redis CLI để kiểm tra dữ liệu:
```sh
docker exec -it redis-jwt redis-cli
```

---

## 🚀 **Các API có sẵn để test**

### **1️⃣ Đăng nhập - Lấy Access Token & Refresh Token**
```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
```
📌 **Phản hồi:**
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "b1c02e91-92d8-44e5-b7aa-5e6a621f9b6c"
}
```

---

### **2️⃣ Lấy Access Token mới bằng Refresh Token**
```http
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "b1c02e91-92d8-44e5-b7aa-5e6a621f9b6c"
}
```
📌 **Phản hồi:**
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "b1c02e91-92d8-44e5-b7aa-5e6a621f9b6c"
}
```

---

### **3️⃣ Đăng xuất (Xóa Refresh Token khỏi Redis)**
```http
POST /auth/logout
Content-Type: application/json

{
  "refreshToken": "b1c02e91-92d8-44e5-b7aa-5e6a621f9b6c"
}
```
📌 **Phản hồi:**
```json
"Logged out successfully"
```

---

## 🎯 **Tổng kết**
✅ **JWT** giúp xác thực người dùng mà không cần lưu session.  
✅ **RSA** giúp tăng cường bảo mật JWT bằng Private/Public Key.  
✅ **Redis** giúp lưu Refresh Token, hỗ trợ quản lý phiên đăng nhập.  
✅ **Docker** giúp cài Redis dễ dàng chỉ với 1 lệnh.

📌 **Giờ bạn có thể dùng Postman hoặc cURL để test API!** 🚀

