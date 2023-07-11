
$(document).ready(function () {
    // 사용자정보 저장용 변수

    ifLiked(userId);
    loginChk(userId);
    let today = initDate(new Date());     // 페이지를 로드한 날짜를 저장

    buildCalendarFrame();
    buildCalendar(today);



    let movieLikeCnt;
    let genres;
    let nations;
    let gradeAge;
    let runningTime;
    let audience;
    let boxOfficeRank;
    let casts;
    let releaseDate;
    let imgSrc;
    let overview;

    // 한번에 보여줄 리뷰 개수와 현재 리뷰 페이지
    let numOfReview = 4;
    let reviewPage = 1;
    // 성인인증 여부 확인


    // 영화시간표를 얻어오기 위해 사용되는 영화관 코드, 전역 변수
    // getAllTable에서 사용됨 , getAllTable은 페이지 로드시 최초1회, 캘린더 날짜 선택시 사용됨
    let cgvCode = localStorage.getItem("cgvCode");
    let megaBoxCode = localStorage.getItem("megaBoxCode");
    let lotteCinemaCode = localStorage.getItem("lotteCinemaCode");
    let cgvArea = localStorage.getItem("cgvArea");
    let megaBoxArea = localStorage.getItem("megaBoxArea");
    let lotteCinemaArea = localStorage.getItem("lotteCinemaArea");

    getInfo(moviePk)
        .then(function () {
            writeLikeCnt(movieLikeCnt);
            getAllTable(movieTitle, parseDate(today));
            posterReleasePlot(movieTitle)
                .then(function () {
                    drawInfo();
                })
                .catch(function () {

                });
        })
        .catch(function (err) {
            // tmbId = 0;
            let info = $(".movieInfoTxt");
            info.empty();
            info.append("<div class='infoErr'><p class='h5 my-5'>영화 정보를 찾을 수 없습니다.</p></div>")
            $("#ticketing").remove();

            posterReleasePlot(movieTitle)
                .then(function () {
                    drawInfo();
                })
                .catch(function () {

                })
        });
    myReviewCardAll(userId);
    makeReviewCardAll(reviewPage, numOfReview, userId);

    // 캘린더의 버튼 클릭시 시간표 출력
    $(".calBtn").on("click", function () {
        $(".calBtn").removeClass("active");
        getAllTable(movieTitle, $(this).val().slice(4))
        $(this).addClass("active");
    });

    // 캘린더 이전 월
    $("#prev").on("click", function () {
        today.setMonth(today.getMonth() - 1);
        buildCalendar(today);
        $(".calBtn").removeClass("active");
        let todayCell = $('button[value="date' + parseDate(new Date()) + '"]');
        todayCell.addClass("active")
    });

    // 캘린더 다음 월
    $("#next").on("click", function () {
        today.setMonth(today.getMonth() + 1);
        buildCalendar(today);
        $(".calBtn").removeClass("active");
        let todayCell = $('button[value="date' + parseDate(new Date()) + '"]');
        todayCell.addClass("active")
    });

    // 관심영화 버튼 클릭시
    $("#likeBtn").on("click", function () {
        // 이미 눌려진 상태일때
        if ($("#likeBtnImg").hasClass("clicked")) {
            $("#likeBtnImg").removeClass("clicked")
            setLike(moviePk, userId, false);
        } else {
            // 안눌려진 상태일때
            $("#likeBtnImg").addClass("clicked")
            setLike(moviePk, userId, true);
        }
    });

    // 모든 영화관 회사의 지역을 불러와서 지역 select에 option 채워넣기
    getAreas("cgv");
    getAreas("lotteCinema");
    getAreas("megaBox");

    // localStorage에 저장된 area 데이터를 가져와서 영화관 select option 채워넣기(없으면 ""으로 인식함)
    getTheaters("cgv", localStorage.getItem("cgvArea"));
    getTheaters("lotteCinema", localStorage.getItem("lotteCinemaArea"));
    getTheaters("megaBox", localStorage.getItem("megaBoxArea"));


    $("#cgvArea").val(localStorage.getItem("cgvArea")).prop("selected", true);

    // cgv, 롯데시네마, 메가박스 버튼을 누르면 시간표가 나오는 div를 보여주고 나머지애들은 d-none 처리하는 함수
    $(".timeTableBtn").on("click", function () {
        // 모든 시간표를 안보이게만듦
        $(".timeTable").addClass("d-none");
        // 누른 버튼의 value (lotteCinema 등)
        const id = $(this).val();

        // localStorage에 지역, 영화관코드가 저장되어있다면 저장된 값으로 option을 선택함
        $("#" + id + "Area").val(localStorage.getItem(id + "Area")).prop("selected", true);
        $("#" + id + "Theater").val(localStorage.getItem(id + "Code")).prop("selected", true);

        // 선택한 영화관회사의 시간표만 보이게만듦
        $("#" + id).removeClass("d-none");
        $(".timeTableBtn").removeClass("active");
        $(this).addClass("active");
    });

    // 지역을 선택하면 영화관이 나오게 하기
    $(".sel1").on("change", function () {
        // selectBox의 id(lotteCinemaArea 등)
        const area = $(this).attr("id");
        // selectBox아래 선택된 option요소의 value(경남/부산/울산 등)
        const selArea = $("#" + area + " option:selected").val();
        const type = area.substring(0, area.length - 4);
        // $("#"+id+"load").removeClass("d-none");
        // $("#"+id+"load").text("로딩 중...");
        getTheaters(type, selArea);

        // ~~Area 전역변수와 locatStroage에 지역을 저장(영화관회사(메가박스 등)을 바꿔서 선택해도
        // select가 이미 선택되어있도록)
        switch (area) {
            case "megaBoxArea":
                megaBoxArea = selArea;
                localStorage.setItem("megaBoxArea", selArea);
                if (selArea == "") {
                    megaBoxCode = "";
                    localStorage.setItem("megaBoxCode", "");
                }
                break;
            case "lotteCinemaArea":
                lotteCinemaArea = selArea;
                localStorage.setItem("lotteCinemaArea", selArea);
                if (selArea == "") {
                    lotteCinemaCode = "";
                    localStorage.setItem("lotteCinemaCode", "");
                }
                break;
            case "cgvArea":
                cgvArea = selArea;
                localStorage.setItem("cgvArea", selArea);
                if (selArea == "") {
                    cgvCode = "";
                    localStorage.setItem("cgvCode", "");
                }
                break;
        }
    });

    // 영화관 이름을 선택하면 테이블 불러옴과 동시에 전역변수 ~~Code에 값 저장, localStorage에 ~~Code 저장
    // 이러면 나중에 캘린더 날짜 변경시 전역변수를 이용하여 테이블을 불러올수있음, detail 페이지 로드시에도 사용됨
    $(".sel2").on("change", function () {
        const code = $(this).val();
        // 누른 버튼의 id값(lotteCinema 등)
        let type = $(this).attr("id");
        type = type.substring(0, type.length - 7);
        const date = $(".calBtn.active").val().slice(4)

        // 코드를 선택하세요 를 제외한 옵션을 선택시 테이블을 가져오고, 아니면 테이블영역을 그냥 둠
        if (code != "") {
            getTable(type, movieTitle, date, code);
        }

        // 영화관코드 전역변수와 로컬스토리지에 선택한 코드를 저장함(달력날짜를 바꿀때 전역변수를 사용해서)
        switch (type) {
            case "megaBox":
                megaBoxCode = code;
                localStorage.setItem("megaBoxCode", code);
                break;
            case "lotteCinema":
                lotteCinemaCode = code;
                localStorage.setItem("lotteCinemaCode", code);
                break;
            case "cgv":
                cgvCode = code;
                localStorage.setItem("cgvCode", code);
                break;
        }
    });

    // 아래는 리뷰작성할때 제목, 내용 칸이 비어있으면 제출버튼 disabled로 바꾸는 함수
    let reviewTitle;
    let reviewContent;
    $("#title").on("propertychange change paste input", function () {
        reviewTitle = $("#title").val();
        reviewContent = $("#content").val();
        if (reviewTitle == "" || reviewContent == "") {
            $("#submitBtn").attr("disabled", "true")
        } else {
            $("#submitBtn").removeAttr("disabled")
        }
    });

    $("#content").on("propertychange change paste input", function () {
        reviewContent = $("#content").val();
        reviewTitle = $("#title").val();
        if (reviewContent == "" || reviewTitle == "") {
            $("#submitBtn").attr("disabled", "true")
        } else {
            $("#submitBtn").removeAttr("disabled")
        }
    });

    // 아래는 리뷰수정할때 제목, 내용 칸이 비어있으면 제출버튼 disabled로 바꾸는 함수
    let reviewTitleEdit;
    let reviewContentEdit;
    $("#titleEdit").on("propertychange change paste input", function () {
        let reviewTitleEdit = $("#titleEdit").val();
        let reviewContentEdit = $("#contentEdit").val();
        if (reviewTitleEdit == "" || reviewContentEdit == "") {
            $("#editSubmitBtn").attr("disabled", "true")
        } else {
            $("#editSubmitBtn").removeAttr("disabled")
        }
    });

    $("#contentEdit").on("propertychange change paste input", function () {
        let reviewContentEdit = $("#contentEdit").val();
        let reviewTitleEdit = $("#titleEdit").val();
        if (reviewTitleEdit == "" || reviewContentEdit == "") {
            $("#editSubmitBtn").attr("disabled", "true")
        } else {
            $("#editSubmitBtn").removeAttr("disabled")
        }
    });

    // 리뷰쓰기
    $("#submitBtn").on("click", function () {
        let reviewWriter = userId;
        let reviewTitle = $("#title").val();
        let reviewContent = $("#content").val();
        let reviewStar = $("#starNum").val();


        if (reviewWriter != "" && reviewContent != "" && reviewTitle != "") {
            reviewTitle = $("#title").val();
            reviewContent = $("#content").val();
            $.ajax({
                url: "/detail/writeReview.do",
                type: "POST",
                data: {
                    "reviewWriter": reviewWriter,
                    "reviewTitle": reviewTitle,
                    "reviewContent": reviewContent,
                    "reviewMoviePk": moviePk,
                    "reviewStar": reviewStar,
                },
                success: function (data) {
                    reviewPage = 1;
                    $("#myReviewSpace .card").remove();
                    $("#reviewSpace .card").remove();
                    myReviewCardAll(userId);
                },
                error: function () {
                    alert("에러발생")
                }
            });
        } else if (reviewWriter == "") {
            alert("로그인하세요");
        } else {
            alert("내용을 입력하세요");
        }
    });

    // 지우기버튼
    $("#erase").on("click", function () {
        $("#submitBtn").attr("disabled", "true");
    });

    // 수정 지우기버튼
    $("#eraseEdit").on("click", function () {
        $("#editSubmitBtn").attr("disabled", "true");
    });

    // 리뷰수정모달확인버튼눌러서 리뷰 수정하기
    $("#editSubmitBtn").on("click", function () {
        let pk = $("#editPk").val();
        let title = $("#titleEdit").val();
        let content = $("#contentEdit").val();
        let starNum = $("#starNum2").val();
        $.ajax({
            url: "/detail/editReview.do",
            type: "PUT",
            data: {
                "reviewPk": pk,
                "reviewTitle": title,
                "reviewContent": content,
                "reviewStar": starNum
            },
            success: function (data) {
                reviewPage = 1;
                $("#myReviewSpace .card").remove();
                $(".reviewMsg").remove();
                myReviewCardAll(userId);
            },
            error: function () {
                console.log("수정 오류")
            }
        });
    });

    // 리뷰수정모달삭제버튼눌러서 리뷰 삭제하기
    // 삭제버튼 눌렀을때
    $(document).on("click", ".delBtn", function () {
        let pk = $(this).attr("id").substring(3);
        let id = userId;
        if (confirm("리뷰를 삭제하시겠습니까?")) {
            $.ajax({
                url: "/detail/delReview.do",
                type: "DELETE",
                data: {
                    "reviewPk": pk,
                    "reviewWriter": id
                },
                success: function (data) {
                    reviewPage = 1;
                    $("#myReviewSpace .card").remove();
                    $("#reviewSpace .card").remove();
                    myReviewCardAll(userId);
                },
                error: function () {
                }
            });
        }

    });

    // 스크롤이벤트 등록, 스크롤발생시 hadleScroll 실행
    $(window).on('scroll', handleScroll);
    function handleScroll() {
        // .scrollTop : 문서의 top 에서부터 현재창까지의 스크롤 위치를 타나냄, 스크롤 위치가 0이면 문서의 최상단을 의미
        var scrT = $(window).scrollTop();
        // console.log(scrT); //스크롤 값 확인용

        // 현재 문서 전체의 높이 - 뷰포트의 높이 : 스크롤바 최하단의 위치
        // (문서 높이가 1000이고 뷰포트 높이가 100이면 화면을 가장 아래로 내렸을때 스크롤바의 위치는 1000-100 = 900임)
        // -300 : 푸터 높이를 고려, 스크롤이 맨 밑에서부터 300만큼 덜 오게되면 리뷰가 불러와짐
        if (scrT >= $(document).height() - $(window).height() - 300) {
            console.log("리뷰 불러오기");
            //스크롤이 끝에 도달했을때 실행될 이벤트
            makeReviewCardAll(reviewPage, numOfReview, userId)
                .then(function(){
                    reviewPage++;
                })
                .catch(function(){});
        }

        // 스크롤 이벤트 해제(1회 실행후 해제)
        $(window).off('scroll', handleScroll);

        // 작업이 완료된 후 스크롤 이벤트 재등록
        // 이럼으로써 스크롤이벤트가 일정시간에 한번씩만 입력되게 함
        // 스크롤을 빠르게 내려서 한번에 주르르륵 실행되게하면 리뷰불러오기가 씹히는 문제 발생(promise를 써도 문제 발생함)
        // 원인은 reviewPage가 한번에 빠르게 증가하면서 불러올 리뷰가 순식간에 마지막페이지가 되면서 db에서 데이터가 안불러와지기 때문인듯
        setTimeout(function() {
            $(window).on('scroll', handleScroll);
        }, 50); // 재등록 딜레이 설정 (ms단위)
    }

    // 로그인 체크하여 각종버튼 비활성화
    function loginChk(id) {
        // 비로그인시 각종버튼 disabled
        console.log("id: " + id);
        if (id == "") {
            $("#likeBtn").attr("disabled", "true");
            $("#likeBtn").addClass("text-dark");
            $("#writeReviewBtn").attr("disabled", "true");
            $("button.reviewLikeBtn").attr("disabled", "true");
        }
    };

    // 캘린더 테이블의 td와 button태그를 그려주는 함수, 최초 1회 실행됨
    function buildCalendarFrame() {
        for (let i = 0; i < 6; i++) {
            // 일 ~ 토 까지 돌기
            for (let j = 0; j < 7; j++) {
                $("#calTr" + i).append("<td class='p-0'><button id='cal" + i + j + "'></button></td>");
            }
        }
    };

    // 캘린더 안에 날짜들을 그려주는 함수, 날짜를 매개변수로 받아 해당 날짜의 월을 표시해줌
    function buildCalendar(today) {
        const firstDateMonth = initDate(new Date(today.getFullYear(), today.getMonth(), 1));

        $("#calYear").text(today.getFullYear());
        $("#calMonth").text(today.getMonth() + 1);

        let temp = firstDateMonth;
        $(".Calendar tbody tr td button").removeClass();
        $(".Calendar tbody tr td button").addClass("calBtn btn border-0");
        $(".Calendar tbody tr td").removeClass();
        $(".Calendar tbody tr td").addClass("p-0");

        // tr 한줄씩 돌기
        for (let i = 0; i < 6; i++) {
            // 일 ~ 토 까지 돌기
            for (let j = 0; j < 7; j++) {
                let cell = $("#calTr" + i + " td button#cal" + i + j);

                // 현재 td의 인덱스가 월 첫째날의 요일 인덱스와 같을경우 data에 값 집어넣기
                if (j == temp.getDay()) {
                    const dateStr = String(temp.getDate()).padStart(2, '0');
                    cell.val("date" + parseDate(temp));
                    cell.text(dateStr);
                    temp.setDate(temp.getDate() + 1);
                }
                // 달의 첫째날이 일요일이 아닌경우 temp에 마이너스 며칠 해가지고 날짜를 맞춰서 temp를 재설정
                else {
                    temp.setDate(temp.getDate() - temp.getDay());
                    const dateStr = String(temp.getDate()).padStart(2, '0');
                    cell.val("date" + parseDate(temp));
                    cell.text(dateStr);
                    temp.setDate(temp.getDate() + 1);
                }
                // 이번달이 아닌 경우
                let tempB = new Date(temp);
                tempB.setDate(temp.getDate() - 1);
                if (tempB.getMonth() > today.getMonth() || tempB.getMonth() < today.getMonth()) {
                    cell.addClass("btn-outline-secondary opacity-50");
                }
                // 일요일
                else if (j == 0) {
                    cell.addClass("btn-outline-danger");
                }
                // 토요일
                else if (j == 6) {
                    cell.addClass("btn-outline-primary");
                }
                // 이번달 평일
                else {
                    cell.addClass("btn-outline-dark");
                }
                // 오늘보다 이전날짜인 경우 회색칠하기
                if (cell.val().slice(4) < parseDate(new Date())) {
                    cell.attr("disabled", "true");
                    cell.parent().addClass("bg-secondary bg-opacity-10");
                } else {
                    cell.removeAttr("disabled");
                    cell.addClass("clickable")
                }
            }
        }
        // 오늘 날짜
        let todayCell = $('button[value="date' + parseDate(new Date()) + '"]');
        todayCell.removeClass();
        todayCell.addClass("calBtn btn border-0 btn-outline-pink todayCell active clickable");
    }

    // Date 객체의 시,분,초,밀리초를 0으로 초기화시켜주는 함수
    function initDate(date) {
        date.setHours(0, 0, 0, 0);
        return date;
    }

    // date를 yyyyMMdd로 변환, padStart는 2자리수, 첫째자리가 비었으면 0으로 채우는 문자열로 변환하는 함수
    function parseDate(date) {
        const returnDate = String(date.getFullYear()) +
            String(date.getMonth() + 1).padStart(2, '0') +
            String(date.getDate()).padStart(2, '0');

        return returnDate;
    }

    // 모든 시간표를 그리는 함수
    // 영화관 코드는 전역변수로 저장된 ~~Code를 사용
    function getAllTable(title, date) {

        $(".timeTable li").remove();

        // let doneList = [false, false, false];
        getTable('cgv', title, date, cgvCode)
        getTable('megaBox', title, date, megaBoxCode);
        getTable('lotteCinema', title, date, lotteCinemaCode);
    }

    // 시간표를 지워주는 함수
    function removeTable(site) {
        $("ul#" + site + "Ul li").remove();
    }

    // 시간표를 그려주는 함수, 영화예매 사이트와 영화제목, 날짜를 매개변수로 받음
    function getTable(site, movieTitle, date, code) {
        $("#" + site + "Load").removeClass("d-none");
        $("#" + site + "Load").text("로딩 중..");
        $.ajax({
            // 접속할 서버의 주소 입력
            url: "/detail/" + site + "/getTimetable.do",
            // 서버와 통신 방식, GET/POST
            type: "GET",
            // 서버로 전송할 매개변수, JSON 방식으로 전송, 자바의 hashmap 타입과 비슷
            // controller에서 RequestParam의 설정값과 동일해야함
            data: {title: movieTitle, date: date, code: code},
            // 통신 성공 시 전달받을 데이터의 타입 설정, json/text/html
            dataType: "json",
            // 통신 성공 시 동작하는 콜백함수
            success: function (data) {
                removeTable(site);
                // 시간표결과가 없으면 정보없다고뜨고 있으면 로딩중... 이라는 p태그 지우기
                if (data.length == 0) {
                    $("#" + site + "Load").removeClass("d-none");
                    $("#" + site + "Load").text("상영 정보가 없습니다.");
                } else {
                    $("#" + site + "Load").addClass("d-none");
                }

                // MovieTimeTableDto 리스트 for문 돌리기
                for (let i = 0; i < data.length; i++) {
                    // 상영관을 표시해주는 ul은 html에 미리 써놨고 상영관의 총 좌석수는 json파일에서 가져옴
                    let ul = $("#" + site + "Ul");
                    let tototalSeat = data[i].totalSeat;

                    // hall을 각 영화관 ul에 li로 추가
                    let hall = "<li class='list-unstyled' id='" + site + i + "'>" + "<span>" + data[i].hall + "</span>" + "</li>";
                    ul.append(hall);

                    // 시간과 잔여좌석이 표시되는 ul태그
                    let hallLi = $("#" + site + i);
                    hallLi.append("<ul class='clearfix'></ul>")
                    let timeUl = $("#" + site + "Ul > #" + site + i + " > ul");
                    let timeTable = data[i].startTimeAndSeatRemain;

                    // MovieTimeTableDto의 시간표인 timeTable for문 돌리기
                    for (let key in timeTable) {
                        let value = timeTable[key];
                        // 시간과 잔여좌석을 하나하나 li로 추가
                        let appendStr = "<li class=' border float-start m-1 py-1 px-2 list-unstyled text-center timeSeat'><strong>" + key + "</strong><br><span>" + value + "/" + tototalSeat + "</span></li>"
                        timeUl.append(appendStr)
                    }
                }
            },
            // 통신 실패 시 동작하는 콜백함수
            error: function () {
                // alert("ajax 통신 중 오류 발생")
            }
        });
    };

    // select에 포함될 지역을 가져와서 그려주는 함수
    function getAreas(type) {
        $.ajax({
            url: "/detail/getAreas.do",
            type: "GET",
            data: {"type": type},
            success: function (data) {
                const select = "#" + type + "Area";
                $(select + " option").remove();
                $(select).append("<option value=''>지역을 선택하세요</option>");
                for (let i = 0; i < data.length; i++) {
                    $(select).append("<option value='" + data[i] + "'>" + data[i] + "</option>");
                }
            },
            error: function () {
                console.log("failed to get areas: " + type);
            }
        });
    }

    // select에 포함될 영화관 이름을 가져와서 그려주는 함수
    function getTheaters(type, area) {
        const select = "#" + type + "Theater";
        $(select + " option").remove();
        $(select).append("<option value=''>영화관을 선택하세요</option>");
        if (area != "") {
            $.ajax({
                url: "/detail/getTheaters.do",
                type: "GET",
                data: {"type": type, "area": area},
                success: function (data) {
                    for (let i = 0; i < data.length; i++) {
                        $(select).append("<option value='" + data[i].theaterCode + "'>" + data[i].theaterName + "</option>");
                    }
                },
                error: function () {
                    console.log("failed to get areas: " + type);
                }
            });
        }
    }

    // 포스터, 줄거리만 추가
    function posterReleasePlot(movieName) {
        return new Promise(function (resolve, reject) {
            let size = movieName.length
            let part1 = movieName.slice(0, size - 1)
            let part2 = movieName.slice(size - 1, size)
            let part3 = movieName.slice(size - 2, size)
            let part4 = movieName.slice(0, size - 2)
            if (isNaN(part2)) {
            } else if (isNaN(part3)) {
                movieName = part1 + " " + part2;
            } else {
                movieName = part4 + " " + part3;
            }
            const urlStr = "https://api.themoviedb.org/3/search/movie?api_key=ad2f7390e457d7dc76e7fda8dcae77b2&language=ko-KR&page=1&query=" + movieName;
            $.ajax({
                url: urlStr,
                type: "GET",
                data: {},
                success: function (data) {
                    const MovieDetailList = data.results;
                    const imageBaseUrl = "https://image.tmdb.org/t/p/w1280";
                    if (MovieDetailList.length > 0) {
                        const result = MovieDetailList[0];
                        imgSrc = imageBaseUrl + result.poster_path + "' alt='" + movieName + " 포스터' class = 'rounded-start";
                        overview = result.overview;
                        releaseDate = result.release_date;
                        resolve(true);
                    }
                },
                error: function () {
                    reject("에러 발생");
                }
            });
        });
    }

    // 다음영화 서버를 이용하여 영화정보를 불러오는 함수, detail페이지가 열릴때 저장되어있던 movie테이블의 영화pk를 사용
    function getInfo(pk) {
        return new Promise(function (resolve, reject) {
            $.ajax({
                url: "/detail/getInfo/" + pk,
                type: "GET",
                data: {},
                success: function (data) {
                    movieTitle = data.movieTitle;


                    movieLikeCnt = data.movieLikeCnt;
                    genres = data.genres;
                    nations = data.nations;
                    gradeAge = data.gradeAge;
                    runningTime = data.runningTime;
                    audience = data.audience;
                    boxOfficeRank = data.boxOfficeRank;
                    casts = data.casts;

                    // 비동기 실행이 정상 실행 시 실행
                    resolve(true);
                },
                error: function () {
                    // 비동기 실행에 오류가 있을 경우 실행
                    console.log("영화 정보 없음")
                    reject("에러 발생");
                }
            });
        });
    }

    // 영화 정보를 써넣어주는 함수, 각 정보들은 전역변수로 저장되어 있어야함(매개변수로 받지 않음)
    function drawInfo() {
        $("#movieTitle").append("<span>" + movieTitle + "</span>");
        $("#releaseDate").append("<span>" + releaseDate + "</span>");
        $("#audience").append("<span>" + noneOr(putComma(audience), " 명") + "</span>");
        $("#boxOfficeRank").append("<span>" + boxOfficeRank + " 위</span>");
        $("#gradeAge").append("<span>" + noneOr(gradeAge, "") + "</span>");
        $("#runningTime").append("<span>" + noneOr(runningTime, " 분") + "</span>");
        $("#overview").append("<p>" + overview + "</p>");
        $("#poster").append("<img class='movie-detail-img shadow-sm' src='" + imgSrc + "' alt='" + movieTitle + " 포스터'/>");

        if (boxOfficeRank == "null") {
            $("#boxOfficeRank").remove();
            $("#ticketing").remove();
        } else {
            $("#ticketing").removeClass("d-none");
        }

        addArraySpan("nations", nations);
        addArraySpan("genres", genres);
        addArraySpan("casts", casts);
    }

    // 배우, 장르를 표시할때 사용됨,id와 배열값을 받고 id요소안에 값1,  값2,  값3 을 span으로 넣어줌 표시해줌
    function addArraySpan(id, array) {
        for (let i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                $("#" + id).append("<span>" + array[i] + ",&nbsp&nbsp</span>");
            } else {
                $("#" + id).append("<span>" + array[i] + "</span>");
            }
        }
    }

    // 콤마 집어넣기
    function putComma(n) {
        let result = "";
        let num = parseInt(n);
        while (parseInt(num / 1000) > 0) {
            result = "," + String(num % 1000).padStart(3, '0') + result;
            num = parseInt(num / 1000);
        }
        result = String(num) + result;
        return result;
    }

    // 영화 정보를 가져왔을때 사용됨, 값이 없으면 정보없음, 값이 있으면 값 + 단위("100 명" 등) 문자열 반환
    function noneOr(str, unit) {
        if (str == null || str == "null" || str == "NaN") {
            return "정보 없음";
        } else {
            return str + unit;
        }
    }

    // 사용자 정보 가져오는 함수
    function setUserInfo() {
        if (sessionStorage.getItem("userId") != null) {
            userId = sessionStorage.getItem("userId");
            userName = sessionStorage.getItem("userName");
            userGrade = sessionStorage.getItem("userGrade");
        }
    }

    // 페이지로드시 좋아요를 눌렀는지 아닌지 확인해서 좋아요버튼 바꿔주는 함수
    function ifLiked(id) {
        if (id != "") {
            $.ajax({
                url: "/detail/getLikedList.do/" + id,
                type: "GET",
                data: {},
                success: function (listStr) {
                    let list = listStr.split(",");
                    localStorage.setItem("likedList", list);
                    if (list.includes(moviePk)) {
                        $("#likeBtnImg").addClass("clicked")
                    }
                },
                error: function () {
                    console.log("좋아요리스트 불러오기 실패");
                }
            });
        }
    }

    // true면 좋아요, false면 좋아요취소
    function setLike(pk, id, type) {
        if (id != "") {
            let listStr = localStorage.getItem("likedList");
            let list = listStr.split(",");
            if (type == true) {
                list.push(pk);
            } else if (type == false) {

                while (list.includes(pk)) {
                    let delIndex = list.indexOf(pk);
                    list.splice(delIndex, 1);
                }
            }

            while (list.includes("")) {
                let delIndex = list.indexOf("");
                list.splice(delIndex, 1);
            }
            localStorage.setItem("likedList", list);
            listStr = list.join();
            $.ajax({
                url: "/detail/setLikedList.do",
                type: "GET",
                data: {"id": id, "likedList": listStr, "pk": pk, "type": type},
                success: function (likeCnt) {
                    writeLikeCnt(likeCnt);
                },
                error: function () {
                    console.log("좋아요리스트 업데이트 실패");
                }
            });
        }
    }

    // likeCnt 숫자 써넣기
    function writeLikeCnt(likeCnt) {
        let pTag = $("#likeCnt");
        pTag.text(putComma(likeCnt));
    }

    // 모든 리뷰 불러와서 만들기
    function makeReviewCardAll(page, num, id) {
            return new Promise(function (resolve) {
                $.ajax({
                    url: "/detail/getReviewList.do",
                    type: "GET",
                    data: {"moviePk": moviePk, "page": page, "num": num, "id": id, "all": "y"},
                    success: function (reviewList) {
                        if (reviewList.length == 0) {
                            let space = $("#reviewSpace");
                            $(".reviewMsg").remove();
                            if (reviewPage == 1 && $("#myReviewSpace").hasClass("d-none")) {
                                space.append("<p class='reviewMsg'>등록된 리뷰가 없습니다.</p>")
                            } else {
                                space.append("<p class='reviewMsg'>마지막 리뷰입니다.</p>")
                            }
                        } else {
                            for (let i = 0; i < reviewList.length; i++) {

                                let review = reviewList[i];
                                makeReviewCard(review.reviewPk, review.reviewTitle, review.reviewWriter, review.reviewContent, review.reviewDate, "", review.reviewEdit, review.reviewLikeCnt, review.reviewStar);
                            }
                            resolve(true);

                        }
                    },
                    error: function () {
                        // console.log("없음")
                    }
                });
            });
    };

    // 리뷰 카드 하나 만들기
    function makeReviewCard(pk, title, name, content, time, id, edit, count, star) {
        let dateTime = time.split(" ")
        let date = dateTime[0];
        let editChk = "";
        let space;

        const thumbsUpIc = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-hand-thumbs-up-fill\" viewBox=\"0 0 16 16\"><path d=\"M6.956 1.745C7.021.81 7.908.087 8.864.325l.261.066c.463.116.874.456 1.012.965.22.816.533 2.511.062 4.51a9.84 9.84 0 0 1 .443-.051c.713-.065 1.669-.072 2.516.21.518.173.994.681 1.2 1.273.184.532.16 1.162-.234 1.733.058.119.103.242.138.363.077.27.113.567.113.856 0 .289-.036.586-.113.856-.039.135-.09.273-.16.404.169.387.107.819-.003 1.148a3.163 3.163 0 0 1-.488.901c.054.152.076.312.076.465 0 .305-.089.625-.253.912C13.1 15.522 12.437 16 11.5 16H8c-.605 0-1.07-.081-1.466-.218a4.82 4.82 0 0 1-.97-.484l-.048-.03c-.504-.307-.999-.609-2.068-.722C2.682 14.464 2 13.846 2 13V9c0-.85.685-1.432 1.357-1.615.849-.232 1.574-.787 2.132-1.41.56-.627.914-1.28 1.039-1.639.199-.575.356-1.539.428-2.59z\"/></svg>"

        // 카드를 만드는 장소: 나의 리뷰인지 전체 리뷰인지
        if (id != "") {
            space = "#myReviewSpace";
        } else {
            space = "#reviewSpace";
        }
        $(space + " .card#review" + pk + id).remove();

        // 카드 만들기
        let spaceItem = $(space);
        spaceItem.append("<div class='card' id='review" + pk + id + "'></div>");
        let card = $("#review" + pk + id);

        // 카드에 버튼 넣고 버튼안에 card-body 만들기
        card.append("<button type='button' class=' text-start reviewDetailBtn border-0 rounded-2 bg-white' data-bs-toggle=\"modal\" data-bs-target=\"#reviewDetail\" value='" + pk + "'><div class='card-body'></div></button>")
        let body = $("#review" + pk + id + " .card-body");

        // 좋아요 버튼이랑 숫자넣기
        body.append("<div class='card-title d-flex '></div>")
        body.find(".card-title").append("<div class='cardStar'>" + starIcon(star, 16) + "</div>");
        body.find(".card-title").append("<span class='reviewLikeIs'>" + thumbsUpIc + "</span>");
        body.find(".card-title .reviewLikeIs").append("<span class='reviewLikeNum ms-2'>" + putComma(count) + "</span>");

        // 카드바디에 제목, 내용 넣기
        body.append("<div class='card-title'><p class='reviewTitle'>" + title + "</p></div>");
        body.append("<div class='card-text content'><p>" + content + "</p></div>");


        // 카드바디에 카드인포(작성자,날짜,수정여부) 넣기
        if (edit == 1) {
            editChk = "(수정됨)"
        }
        body.append("<div class='card-text info'><span class='date'>" + date + "</span><span class='edit'>" + editChk + "</span><span class='name'>by " + name + "</span></div>");
    }

    // 최초 글쓰기 모달창에 별아이콘 만들기
    $("#starShow").append(starIcon(1, 48));

    // .star 클래스의 투명한 버튼에 마우스를 올리면 아이콘(svg)파일을 모두 지운 뒤 버튼의 value(1~10까지)에 따라 별아이콘을 새로 만듦
    $(".star").hover(function () {
        $("#starShow svg").remove();
        $("#starShow").append(starIcon($(this).val(), 48));
    },
        // 버튼에서 마우스를 뗐을 때는 입력된 값에 따라서 별 아이콘을 다시 그림
        function(){
            $("#starShow svg").remove();
            $("#starShow").append(starIcon($("#starNum").val(), 48));
        });

    // 버튼 클릭 시 값을 input태그에 입력
    $(".star").on("click", function () {
        $("#starNum").val($(this).val());
    });


    // 수정 모달 창에 위와 같은 내용 적용
    $(".star2").hover(function () {
        $("#starShow2 svg").remove()
        $("#starShow2").append(starIcon($(this).val(), 48));
    }, function(){
        $("#starShow2 svg").remove();
        $("#starShow2").append(starIcon($("#starNum2").val(), 48));
    });

    $(".star2").on("click", function () {
        $("#starNum2").val($(this).val());
    });

    // 별점 아이콘 만들기
    function starIcon(starNum, size) {
        let empty = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + size + "\" height=\"" + size + "\" fill=\"currentColor\" class=\"bi bi-star\" viewBox=\"0 0 16 16\"><path d=\"M2.866 14.85c-.078.444.36.791.746.593l4.39-2.256 4.389 2.256c.386.198.824-.149.746-.592l-.83-4.73 3.522-3.356c.33-.314.16-.888-.282-.95l-4.898-.696L8.465.792a.513.513 0 0 0-.927 0L5.354 5.12l-4.898.696c-.441.062-.612.636-.283.95l3.523 3.356-.83 4.73zm4.905-2.767-3.686 1.894.694-3.957a.565.565 0 0 0-.163-.505L1.71 6.745l4.052-.576a.525.525 0 0 0 .393-.288L8 2.223l1.847 3.658a.525.525 0 0 0 .393.288l4.052.575-2.906 2.77a.565.565 0 0 0-.163.506l.694 3.957-3.686-1.894a.503.503 0 0 0-.461 0z\"/></svg>";
        let half = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + size + "\" height=\"" + size + "\" fill=\"currentColor\" class=\"bi bi-star-half\" viewBox=\"0 0 16 16\"><path d=\"M5.354 5.119 7.538.792A.516.516 0 0 1 8 .5c.183 0 .366.097.465.292l2.184 4.327 4.898.696A.537.537 0 0 1 16 6.32a.548.548 0 0 1-.17.445l-3.523 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256a.52.52 0 0 1-.146.05c-.342.06-.668-.254-.6-.642l.83-4.73L.173 6.765a.55.55 0 0 1-.172-.403.58.58 0 0 1 .085-.302.513.513 0 0 1 .37-.245l4.898-.696zM8 12.027a.5.5 0 0 1 .232.056l3.686 1.894-.694-3.957a.565.565 0 0 1 .162-.505l2.907-2.77-4.052-.576a.525.525 0 0 1-.393-.288L8.001 2.223 8 2.226v9.8z\"/></svg>";
        let full = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + size + "\" height=\"" + size + "\" fill=\"currentColor\" class=\"bi bi-star-fill\" viewBox=\"0 0 16 16\"><path d=\"M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z\"/></svg>";

        let starIcons = "";
        let emptyIcons = 10 - starNum;
        for (let i = 0; i < Math.trunc(starNum / 2); i++) {
            starIcons = starIcons + full;
        }
        if (starNum % 2 == 1) {
            starIcons = starIcons + half;
        }
        for (let i = 0; i < Math.trunc(emptyIcons / 2); i++) {
            starIcons = starIcons + empty;
        }
        return starIcons;
    }


    // 리뷰 상세 모달 열기
    $(document).on("click", ".reviewDetailBtn", function () {
        const title = $(this).find(".card-title .reviewTitle").text();
        const content = $(this).find(".card-text.content p").text();
        const value = $(this).val();
        let likeCnt = $(this).find(".reviewLikeNum").text();
        const reviewStar = $(this).find(".cardStar").clone();
        $("#reviewDetail .modal-header span.modalStar").empty();
        $("#reviewDetail .modal-header span.modalStar").append(reviewStar);
        $("#reviewDetail .modal-header span.modalTitle").text(title);
        $("#reviewDetail .modal-body p").text(content);
        $("#reviewDetail .modal-body button.reviewLikeBtn").val(value);
        $("#reviewDetail .modal-body .reviewLikeModal .reviewLikeNum").remove();
        $("#reviewDetail .modal-body .reviewLikeModal").append("<p class='reviewLikeNum my-1'>" + putComma(parseInt(likeCnt.replace(/,/g, ""))) + "</p>");
        ifLikedReview(userId, value);
    });

    // 리뷰좋아요클릭
    $(document).on("click", ".reviewLikeBtn", function () {

        if (userId != "") {
            if ($(this).hasClass("active")) {
                // 좋아요 -1, 리스트에서 id 빼서 값전송
                updateReviewLike(false, userId, $(this).val());
                $(this).removeClass("active")
            } else {
                // 좋아요 +1, 리스트에 id 추가해서 전송
                updateReviewLike(true, userId, $(this).val());
                $(this).addClass("active")
            }
            // ifLikedReview(userId,$(this).val())
        }
    });

    // 리뷰 좋아요 클릭했을때
    function updateReviewLike(type, id, reviewPk) {
        if (id != "") {
            let likedStr = localStorage.getItem("reviewLikeList");
            let likedList = likedStr.split(",");
            if (type == true) {
                likedList.push(reviewPk);
            } else if (type == false) {
                while (likedList.includes(reviewPk)) {
                    let delIndex = likedList.indexOf(reviewPk);
                    likedList.splice(delIndex, 1);
                }
            }
            while (likedList.includes("")) {
                let delIndex = likedList.indexOf("");
                likedList.splice(delIndex, 1);
            }
            localStorage.setItem("reviewLikeList", likedList);
            likedStr = likedList.join();
            $.ajax({
                url: "/detail/updateReviewLike.do",
                type: "GET",
                data: {"type": type, "id": id, "reviewPk": reviewPk, "likedList": likedStr},
                success: function (reviewCnt) {
                    let card = $("div.card[id^=review" + reviewPk + "]").find(".reviewLikeNum");
                    card.text(putComma(reviewCnt));
                    let likeNumModal = $("div.reviewLikeModal .reviewLikeNum");
                    likeNumModal.text(putComma(reviewCnt));
                },
                error: function () {
                    console.log("리뷰좋아요리스트 불러오기 실패");
                }
            });
        }

    }

    // 리뷰 좋아요를 눌렀는지 아닌지 확인해서 좋아요버튼 바꿔주는 함수
    function ifLikedReview(id, reviewPk) {
        $(".reviewLikeBtn").removeClass("active");
        $(".reviewLikeBtn").finish();
        if (id != "") {
            $.ajax({
                url: "/detail/getReviewLike.do",
                type: "GET",
                data: {"id": id},
                success: function (listStr) {
                    let list = listStr.split(",");
                    localStorage.setItem("reviewLikeList", list);
                    if (list.includes(reviewPk)) {
                        $(".reviewLikeBtn").addClass("active")
                        $(".reviewLikeBtn").finish();
                    }
                },
                error: function () {
                    console.log("리뷰좋아요리스트 불러오기 실패");
                }
            });
        }
    }

    // 내가쓴리뷰 카드 만들기
    function myReviewCardAll(id) {
        if (id != "") {
            $.ajax({
                url: "/detail/getReviewList.do",
                type: "GET",
                data: {"moviePk": moviePk, "page": 1, "num": 100, "id": id, "all": "n"},
                success: function (reviewList) {

                    if (reviewList.length == 0) {
                        $("#myReviewSpace").addClass("d-none");
                        $(".myReview").addClass("d-none");
                    } else {
                        $("#myReviewSpace p").removeClass("d-none");
                        for (let i = 0; i < reviewList.length; i++) {
                            let review = reviewList[i];
                            makeReviewCard(review.reviewPk, review.reviewTitle, review.reviewWriter, review.reviewContent, review.reviewDate, id, review.reviewEdit, review.reviewLikeCnt, review.reviewStar);
                            $("#review" + review.reviewPk + id).append("<div class='card-footer'></div>")
                            $("#review" + review.reviewPk + id + " .card-footer")
                                .append(
                                    "<button " +
                                    "data-bs-toggle=\"modal\" " +
                                    "data-bs-target=\"#editReview\" " +
                                    "type='button' " +
                                    "class='editBtn btn opacity-50 border-0' " +
                                    "value='edit" + review.reviewPk + id + "' " +
                                    "id='edit" + review.reviewPk + "'>" +
                                    "수정" +
                                    "</button>");
                            $("#review" + review.reviewPk + id + " .card-footer")
                                .append("<button class='delBtn btn border-0 opacity-50' id='del" + review.reviewPk + "'>삭제</button>")
                        }
                    }
                },
                error: function () {
                    // console.log("없음")
                }
            });
        } else {
            $("#myReviewSpace").addClass("d-none");
            $(".myReview").addClass("d-none");
        }
    }

    // 동적으로 추가된 버튼에 적용하는 코드
    // 수정버튼 눌렀을 때
    $(document).on("click", ".editBtn", function () {

        let pk = $(this).attr("id").substring(4);
        let itemId = $(this).val().substring(4);
        let title = $("#review" + itemId + " .card-title p").text();
        let content = $("#review" + itemId + " .content p").text();


        let cardStar = $("#review" + itemId + " .cardStar")
        let starNum = cardStar.find(".bi-star-fill").length * 2 + cardStar.find(".bi-star-half").length;

        $("#titleEdit").val(title);
        $("#contentEdit").val(content);
        $("#editPk").val(pk);
        $("#starShow2 svg").remove();
        $("#starShow2").append(starIcon(starNum, 48));
        $("#starNum2").val(starNum);
    });

});