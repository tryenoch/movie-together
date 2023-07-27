function conv(movieName){
    let result = "";
    switch (movieName){
        case "예시":
            result = "결과";
            break;
        case "명탐정코난: 흑철의 어영":
            result = "명탐정 코난: 흑철의 어영";
            break;
    }
    return result;
}

export default conv;