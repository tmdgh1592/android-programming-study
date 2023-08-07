# 데이터베이스와 Room 라이브러리

모든 어플리케이션은 장시간 동안 데이터를 저장할 곳이 필요하다.
기존에는 ViewModel과 SIS(Saved Instance State)를 사용해서 일시적인 UI 상태 데이터를 지속하는 방법을 사용했다.
위 두 가지 방법은 크기가 작은 데이터에는 아주 유용하지만, UI 상태와 무관하게 영구적으로 저장할 수 없고, 크기가 큰 데이터는 무리가 생길 수 있다.

## Room 아키텍처 컴포넌트 라이브러리

Room은 Jetpack의 아키텍처 컴포넌트 라이브러리이다.
어노테이션을 지정하여, SQLiteDatabase를 더 간편하게 쿼리할 수 있도록 제공한다.

Room의 구성요소
  - API : 데이터베이스를 정의하고 인스턴스를 생성하기 위해 상속받는 클래스
  - 어노테이션 : Entity, Database 등을 나타내기 위한 어노테이션
  - 컴파일러 : 지정한 어노테이션을 컴파일해서 실제 데이터베이스 구현체(클래스나 인터페이스 등)을 생성한다.

Room을 사용하기 위해서 `room-runtime` / `room-compiler` 의존성을 추가해야 한다.
또한, 어노테이션을 처리하기 위해 `kotlin-kapt` 플러그인을 추가한다. 플러그인은 안드로이드 스튜디오 같은 IDE에 기능을 추가하는 방법이다.

- room-runtime : 데이터베이스를 정의하는 데 필요한 모든 클래스와 어노테이션을 포함하는 Room API다.
- room-compiler : 지정한 어노테이션을 컴파일해서 데이터베이스 구현체(클래스, 인터페이스)를 생성한다.
  - room-compiler 의존성을 지정할 때는 implementation 대신 kapt 키워드를 사용해 Room 컴파일러가 생성한 클래스들을 안드로이드 스튜디오가 알 수 있게 한다.
 
## 데이터베이스 생성하기
Room으로 데이터베이스를 생성할 때는 다음 세 단계로 한다.
1. 모든 클래스에 어노테이션을 지정해 데이터베이스 엔티티(Entity)로 만든다.
2. 데이터베이스 자체를 나타내는 클래스를 생성한다.
3. 데이터베이스가 모델 데이터를 처리할 수 있게 타입 변환기(type converter)를 생성한다.

### 엔티티 정의하기
Room은 우리가 정의한 엔티티를 기반으로 데이터베이스 테이블 구조를 만든다.
@Entity 어노테이션으로 지정하면, Room이 해당 클래스의 데이터베이스 테이블을 생성한다.

```kotlin
@Entity
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false)
```
Crime 테이블의 각 행이 하나의 Crime 객체를 나타낸다.
클래스에 정의된 각 속성은 테이블의 열이므로 속성 이름은 열의 이름이 된다.
따라서, Crime 객체 데이터의 id, title, date isSolved 열을 갖는다.

### 데이터베이스 클래스 생성하기
엔티티 클래스는 데이터베이스 테이블 구조를 정의한다.
만약 앱에 여러 데이터베이스가 있다면 특정 엔티티 클래스는 여러 데이터베이스에서 사용될 수 있다. (흔하지는 않지만 가능하다.)
이런 이유로 엔티티 클래스를 데이터베이스와 연관시켜 주어야 Room이 테이블을 생성하는 데 사용할 수 있다.

```kotlin
@Database(entities = [Crime::class], version = 1)
abstract class CrimeDatabase : RoomDatabase() {
   ...
}
```

@Database 어노테이션은 해당 클래스가 앱의 데이터베이스를 나타낸다고 Room에게 알려준다.
- `entities` : 데이터베이스의 테이블을 생성하고 관리하는 데 사용할 엔티티 클래스들을 지정한다.
- `version` : 데이터베이스 버전을 지정한다. 데이터베이스를 처음 생성했을 때는 버전이 1이다. 그리고 앱을 계쏙 개발하는 동안 새로운 엔티티를 추가하거나, 기존 엔티티를 변경하면 버전 번호를 증가시켜야 한다.

### DAO 만들기
- Database와 Entity(Table)를 만들었다면, 데이터베이스의 테이블에 접근할 수 있는 객체가 필요하다. 이를 DAO라고 부른다.
```kotlin
@Dao
interface CrimeDao {

  @Query("SELECT * FROM crime")
  fun  getCrimes(): List<Crime>

  @Query("SELECT * FROM crime WHERE id=(:id)")
  fun getCrime(id: UUID): Crime?
}
```

다음으로 CrimeDao를 CrimeDatabase에 추가해야 한다.
```kotlin
@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
   abstract fun crimeDao(): CrimeDao
}

## 리포지터리 패턴으로 데이터베이스 액세스하기

리포지터리 패턴은 단일 또는 여러 소스로부터 데이터를 액세스하는 로직을 캡슐화하고, 로컬이나 원격 서버로부터 특정 데이터를 가져오거나 저장하는 방법을 결정한다.
UI는 어떻게 데이터를 저장하거나 가져오는지 관연하지 않으므로, 이런 일은 리포지토리에서 구현한다.

```kotlin
class CrimeRepository private constructor(context: Context) {
    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
          if (INSTANCE == null) {
              INSTANCE = CrimeRepository(context)
          }
        }

        fun get(): CrimeRepository {
          return INSTANCE ?: throw IllegalStateException("...")
        }
    }
}
```

CrimeRepository는 싱글톤이다. 앱이 실행되는 동안 하나의 인스턴스만 생성된다는 의미다.
싱글톤이 갖는 속성은 액티비티나 프래그먼트의 생명주기 상태가 변경되어도 계속 유지될 수 있고, OS가 메모리에서 앱을 제거하면 싱글톤도 함께 소멸한다.

`Room.databaseBuilder(...)`를 통해 CrimeDatabase의 실체를 만들 수 있다.
- 첫 번째 매개변수 : 데이터베이스의 context, 주로 applicationContext를 지정하여, 앱의 실행부터 종료까지 유지한다.
- 두 번째 매개변수 : Room으로 생성하고자 하는 데이터베이스 클래스 (CrimeDatabase::class.java)
- 세 번째 매개변수 : Room으로 생성하고자 하는 데이터베이스 파일명 (ex. crime-database) -> 이후에 crime-database.db로 저장된다.
