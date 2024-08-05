# Đọc nội dung của file
with open('a.txt', 'r') as file:
    lines = file.readlines()

# Loại bỏ các dòng trùng nhau
unique_lines = list(set(lines))

# Ghi các dòng không trùng vào file mới
with open('a_unique.txt', 'w') as file:
    file.writelines(unique_lines)
