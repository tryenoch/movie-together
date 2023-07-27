// 추가 : pjh
// tmdb에 사용될 영화제목의 경우에 conv(영화제목) 함수를 사용하면 됨
// 영화진흥원에 등록된 영화제목과 tmdb에 등록된 영화제목이 다를 경우 사용됨
// 영화진흥원에 등록된 영화제목은 롯데시네마, cgv, 메가박스, 다음에서 그대로 사용됨

// 숫자로 시리즈가 표기된 경우 숫자와 그앞의 문자열 사이에 공백이 있어야 tmdb에서 검색됨 -> 공백을 자동으로 넣어줌
// 규칙이 없는 경우 자동변환이 불가능하므로 switch문에 수동으로 추가해줘야함

// 추가방법
// case "영화진흥원에 등록된 영화제목" :
// result = "tmdb에 등록된 영화제목";
// break;

function conv(mn){
     let name = mn;

    // (문자열)(숫자) 조합의 영화제목일 경우 문자열과 숫자 사이에 공백을 추가함
    // ex) "핑크퐁 시네마 콘서트3 : 진저브레드맨을 잡아라" -> "핑크퐁 시네마 콘서트 3 : 진저브레드맨을 잡아라"
    if(name.length > 1){
        for(let i = 1; i < name.length; i ++){
            // 공백일 경우 isNaN() 값이 false가 되므로, 공백이 아닐 경우만 적용되도록 if문 조건에 추가
            if(!isNaN(name[i]) && isNaN(name[i-1]) && name[i] != " "){
                const temp = name.substring(0,i) + " " + name.substring(i);
                name = temp;
                break;
            }
        }
    }

    let result;

    switch (name){
        case "예시":
            result = "결과";
            break;

        case "명탐정코난: 흑철의 어영":
            result = "명탐정 코난: 흑철의 어영";
            break;

        default:
            result = name;
            break;
    }
    return result;
}

