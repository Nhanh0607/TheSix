// Hàm thêm vào giỏ hàng sử dụng Fetch API (AJAX) để không load lại trang
function addToCart(id) {
    // Gọi API thêm vào giỏ (Bạn cần sửa Controller một chút để trả về JSON nếu muốn hoàn hảo, 
    // nhưng ở đây ta gọi link cũ và dùng hiệu ứng giả lập cho đẹp trước)
    
    fetch(`/cart/add/${id}`)
        .then(response => {
            if(response.ok) {
                // Sử dụng SweetAlert2 (Plugin) để hiện thông báo đẹp
                Swal.fire({
                    title: 'Thành công!',
                    text: 'Đã thêm sản phẩm vào giỏ hàng Cyber!',
                    icon: 'success',
                    background: '#1e293b',
                    color: '#fff',
                    confirmButtonColor: '#06b6d4',
                    timer: 1500,
                    showConfirmButton: false,
                    backdrop: `rgba(0,0,123,0.4)`
                }).then(() => {
                    // Load lại trang để cập nhật số lượng trên menu
                    location.reload(); 
                });
            } else {
                Swal.fire('Lỗi', 'Không thể thêm vào giỏ', 'error');
            }
        });
}