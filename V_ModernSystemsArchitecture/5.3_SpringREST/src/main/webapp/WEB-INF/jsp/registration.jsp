<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Регистрация студента</title>
    <style>
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 150px; }
    </style>
</head>
<body>
    <div>
        <h2>Регистрация студента</h2>

        <form:form method="POST" modelAttribute="userForm">
            <div class="form-group">
                <label for="firstName">Имя:</label>
                <form:input path="firstName" id="firstName"/>
                <form:errors path="firstName"></form:errors>
                    ${firstNameError}
            </div>

            <div class="form-group">
                <label for="lastName">Фамилия:</label>
                <form:input path="lastName" id="lastName"/>
            </div>

            <div class="form-group">
                <label for="patronymic">Отчество:</label>
                <form:input path="patronymic" id="patronymic"/>
            </div>

            <div class="form-group">
                <label for="birthDate">Дата рождения:</label>
                <form:input path="birthDate" type="date" id="birthDate"/>
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <form:input path="email" type="email" id="email"/>
            </div>

            <div class="form-group">
                <label for="password">Пароль:</label>
                <form:password path="password" id="password"/>
                <form:errors path="password"></form:errors>
                    ${passwordError}
            </div>

            <div class="form-group">
                <label for="passwordConfirm">Подтвердите пароль:</label>
                <form:password path="passwordConfirm" id="passwordConfirm"/>
                <form:errors path="password"></form:errors>
                    ${passwordError}
            </div>

            <button type="submit">Зарегистрироваться</button>
        </form:form>

        <a href="/">На главную</a>
    </div>

    <!-- Блок успешной регистрации (изначально скрыт) -->
    <c:if test="${registrationSuccess}">
        <div class="success-message">
            <p>✅ Регистрация прошла успешно!</p>
            <p class="redirect-message">
                Через <span id="countdown">5</span> секунд вы будете перенаправлены в личный кабинет...
            </p>
            <p>Используйте Ваш email для входа</p>
            <p>Если перенаправление не сработало, перейдите по ссылке: <a href="/api/v1/students/lk/${studentId}">Личный кабинет</a>.</p>
        </div>

        <script>
            // Таймер обратного отсчёта
            let seconds = 5;
            const countdownElement = document.getElementById('countdown');

            const timer = setInterval(function() {
                seconds--;
                countdownElement.textContent = seconds;

                if (seconds <= 0) {
                    clearInterval(timer);
                    window.location.href = "/api/v1/students/lk/${studentId}";
                }
            }, 1000);
        </script>
    </c:if>
</body>
</html>