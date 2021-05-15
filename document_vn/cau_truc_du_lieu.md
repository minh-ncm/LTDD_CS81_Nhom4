# Cấu trúc dữ liệu:
Dưới đây là các collections được lưu trong firestore:  

root    
|__user  
|__news  
|           |__contents  
|           |__images  
|_comments  

## Trong comments gồm các thuộc tính:
- authorUsername: tên đăng nhập của tác giả bài báo được comment
- title: tên của bài báo được comment
- username: tên đăng nhập của người ghi comment
- donwnvote: số lượng đồng tình/thích
- upvote: số lượng không đồng ý/phản đối
- comment: nội dung của comment

## Trong user gồm các thuộc tính:
- username: tên đăng nhập của user
- password: mật khẩu của user
- name: tên của user
2 thuộc tính tiếp theo để mở rộng thêm, chưa được hiện thực.
- isAdmin: có phải là admin không
- isWriter: có phải là ký giả không

## Trong news gồm:
### thuộc tính:
- authorUsername: tên đăng nhập của tác giả
- title: tên bài báo
- type: loại báo
- createedDate: ngày viết
- previewContent: đoạn ngắn dùng để xem trước bài báo
- thumbnailURL: link chứa ảnh được dùng để làm trang bìa

### tập hợp:
Việc có contents và images riêng giúp giảm dữ liệu truy xuất khi không cần load hết bài báo.
contents: lưu nội dung tác giả viết được tách thành từng đoạn nhỏ.
Chia thành nhiều đoạn nhỏ để có thể mở rộng về sau, giúp thêm chức năng thêm ảnh vào giữa 2 đoạn trong bài báo.
images: lưu danh sách url của ảnh có trong bài báo