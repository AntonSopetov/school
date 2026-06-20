-- 1. Получить информацию обо всех студентах (имя, возраст) вместе с названиями факультетов
-- Используем LEFT JOIN, чтобы в выборку попали даже те студенты, у которых факультет не назначен
SELECT s.name AS student_name, s.age, f.name AS faculty_name
FROM student s
         LEFT JOIN faculty f ON s.faculty_id = f.id;

-- 2. Получить только тех студентов, у которых есть аватарки
-- Используем INNER JOIN, так как нам нужны только те строки, для которых есть совпадение в таблице аватарок
SELECT s.name AS student_name, s.age
FROM student s
         INNER JOIN avatar a ON a.student_id = s.id;