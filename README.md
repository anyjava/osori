osori
==============

OSORI는 권한 관리를 쉽게 해주는 플랫폼으로 UI형태의 관리 페이지와 라이브러리를 제공함으로써 최소한의 비용으로 연동할 수 있도록 개발되었습니다.

Modules
-------
OSORI프로젝트는 3개의 모듈로 구성되어있습니다.

### osori-admin
FreeMarker+SpringBoot+JPA로 만들어진 모듈로써 가장 큰부분을 담당하고있으며 아래와 같은 기능을 담당합니다.
* UI기반의 관리페이지
* 관리페이지의 내부 액션을 처리하기위한 API 제공
* 외부로부터 권한체크 상호작용을 위한 API 제공

### osori-client-core
osori-admin과 연동을 담당하는 클라이언트 라이브러리로 URI기반 혹은 osori-admin에서 설정한 URI의 id값으로 권한체크를 지원합니다.

### osori-client-spring
Spring기반의 프로젝트를 위한 라이브러리로 Interceptor, Filter형태로 제공되어 osori-admin과 연동을 지원합니다.

Requirements
-------------
[Requirements Page](https://github.com/woowabros/platform-osori/wiki/Requirements)

Quick Start
---------------------
osori-admin은 JDK1.8 그외 클라이언트 라이브러리들은 JDK1.7기반으로 작성되었습니다.
osori-admin은 SpringBoot기반으로 개발되어있어서 한번 빌드후 별다른 설정없이 `java -jar`커맨드로 어플리케이션을 실행할 수 있습니다.

`$ ./gradlew clean build`

`$ java -jar osori-admin.jar` 혹은 `/osori-admin/run-h2.sh`을 통해서 실행이 가능합니다. (내장 H2가 아닌 mysql을 사용하길 원한다면 /osori-admin/run-mysql.sh을 환경에 맞게 수정후 실행도 가능합니다)

Admin Guide
------------
[Admin Guide](https://github.com/woowabros/platform-osori/wiki/Admin-Guide)

Development Notes
------------------
[Development Notes](https://github.com/woowabros/platform-osori/wiki/Reference)
