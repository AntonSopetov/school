-- 1. Возраст студента не может быть меньше 16 лет
ALTER TABLE student
    ADD CONSTRAINT check_student_age CHECK (age >= 16);

-- 2. Имена студентов должны быть уникальными и не равны нулю (NOT NULL)
ALTER TABLE student
    ALTER COLUMN name SET NOT NULL,
ADD CONSTRAINT unique_student_name UNIQUE (name);

-- 3. Пара "значение названия" - "цвет факультета" должна быть уникальной
ALTER TABLE faculty
    ADD CONSTRAINT unique_name_color UNIQUE (name, colour);

-- 4. При создании студента без возраста ему автоматически должно присваиваться 20 лет
ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;