<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="icon" type="image/svg" href="./assets/img/icon-aptech.svg">
    <title>
        Aptech88 - website
    </title>
    <!--     Fonts and icons     -->
    <link rel="stylesheet" type="text/css"
        href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700,900|Roboto+Slab:400,700" />

    <!-- Font Awesome Icons -->
    <script src="https://kit.fontawesome.com/42d5adcbca.js" crossorigin="anonymous"></script>
    <!-- Material Icons -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">
    <link rel="stylesheet" href="https://unpkg.com/swiper@7/swiper-bundle.min.css" />
    <!-- CSS Files -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.6.1/font/bootstrap-icons.css">
    <link href='https://unpkg.com/boxicons@2.0.9/css/boxicons.min.css' rel='stylesheet'>

</head>
<style>
    .error-block {
        display: grid;
        place-items: center;
    }

    .error-block h1 {
        font-size: 200px;
        letter-spacing: 10px;
    }

    .error-block-desc {
        font-size: 50px;
        font-weight: lighter;
    }

    .error-block a {
        padding: 18px 32px;
        box-shadow: 0 10px 50px #ddc179;
    }

    .error-block a:hover {
        opacity: 0.8;
    }
</style>

<body>
    <main class="main-content position-relative border-radius-lg  ">
        <div class="error-block mt-5 mb-5">
            <div class="error-block-title">
                <h1 class="text-success">Success!</h1>
            </div>
            <div class="error-block-desc ">
                Your Invoice has been paid.
            </div>
            <a type="button" class="btn btn-warning mt-5">Back to home <i class='bx bx-home-heart'></i></a>
        </div>
    </main>
    <main class="main-content position-relative border-radius-lg  ">
        <div class="error-block mt-5 mb-5">
            <div class="error-block-title">
                <h1 class="text-danger">Error!</h1>
            </div>
            <div class="error-block-desc ">
                Your method have an error.
            </div>
            <a type="button" class="btn btn-warning mt-5">Back to home <i class='bx bx-home-heart'></i></a>
        </div>
    </main>
</body>

</html>