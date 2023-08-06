# Chapter11 데이터베이스와 Room 라이브러리

## Room 아키텍처 컴포넌트 라이브러리
- Room은 Jetpack의 아키텍처 컴포넌트 라이브러리
- UI 상태와 무관하게 영구적으로 지속될 필요가 있는 데이터의 저장에 사용
- API, 애노테이션(annotation), 컴파일러로 구성되어 있다.

### kotlin-kapt
- 코틀린 애노테이션 처리 도구(Kotlin Annotation Processor tool)
- 라이브러리가 생성한 클래스들은 안드로이드 스튜디오에서 알 수가 없어 kotlin-kapt 플러그인을 추가하면 라이브러리가 생성한 클래스를 import 해서 사용할 수 있다.

### room-runtime
- 데이터베이스를 정의하는 데 필요한 모든 클래스와 애노테이션을 포함하는 Room API다.

### room_compiler
- 지정한 애노테이션을 컴파일해서 데이터베이스 구현체(클래스나 인터페이스 등)를 생성한다.
- room-compiler 의존성을 지정할 땐 implementation 대신 kapt 키워드를 사용해 Room 컴파일러가 생성한 클래스들을 안드로이드 스튜디오가 알 수 있게 한다.

## 데이터베이스 생성하기
1. 모델 클래스에 애노테이션을 지정해 데이터베이스 엔티티(entity)로 만든다.
2. 데이터베이스 자체를 나타내는 클래스를 생성한다.
3. 데이터베이스가 모델 데이터를 처리할 수 있게 타입 변환기(type converter)를 생성한다.

### 엔티티 정의하기
- Room은 우리가 정의한 엔티티를 기반으로 앱의 데이터베이스 테이블 구조를 만든다.
- 엔티티는 우리가 생성하는 모델 클래스로 @Entity 애노테이션으로 지정한다.
- 그럼 이 애노테이션으로 Room이 해당 클래스의 데이터베이스 테이블을 생성한다.

```kotlin
@Entity
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false)
```

> 테이블의 행은 각 Crime 객체가 되고 열은 속성(id, title, data, isSolved)가 된다.

## 데이터베이스 클래스 생성하기
- 엔티티 클래스는 데이터베이스 테이블의 구조를 정의한다.
- 엔티티 클래스를 데이터베이스와 연관시켜 주어야 Room이 테이블을 생성하는 데 사용할 수 있다.

```kotlin
@Database(entities = [ Crime::class ], version = 1)
abstract class CrimeDatabase : RoomDataBase() {
}
```

- @Database 애노테이션은 이 클래스가 앱의 데이터베이스를 나타낸다고 Room에게 알려준다.
- 그리고 이 애노테이션은 두 개의 매개변수를 지정해야 한다. 
- 첫 번째 매개변수에는 엔티티 클래스들을 지정한다.
- 두 번쨰 매개변수에는 데이터베이스의 버전을 지정한다. 

### 타입 변환기 생성하기
- Room은 내부적으로 SQLite를 사용한다. SQLite는 MySQL과 같은 오픈 소스 관계형 데이터베이스다.(Structured Query Language)
- Room은 코틀린 객체와 데이터베이스 사이에서 객체-관계 매핑(ORM Object-Relational-Mapping) 계층의 역할을 하면서 SQL 사용을 쉽게 해준다.
- Room 기본 데이터 타입 이외에 Date와 UUID 타입 속성이 있다. 이런 타입의 데이터를 데이터베이스 테이블에 저장하거나 가져오는 방법을 Room에게 알려주어야 한다.
- Room에게 특정 타입을 데이터베이스에 저장되는 타입으로 변환하는 방법을 알려준다.
- 각 타입에 대해 @TypeConverter 애노테이션이 지정된 두 개의 함수가 필요한데, 데이터베이스에 데이터를 저장하기 위해 타입을 변환하는 함수와 데이터베이스로부터 읽은 데이터를 원하는 타입으로 변환하는 함수이다.

```kotlin
class CrimeTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }
}
```

타입 변환기 클래스를 데이터베이스 클래스에 추가한다.
```kotlin
@Database(entities = [ Crime::class ], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDataBase() {
}
```

## DAO 정의 하기
- 데이터베이스 테이블의 데이터를 액세스하려면 DAO(Data Access Object)를 생성해야 한다.
- 데이터베이스 작업을 수행하는 함수들을 포함하는 인터페이스다. 

```kotlin
@Dao
interface CrimeDao {
    
    @Query("SELECT * FROM crime")
    fun getCrimes(): List<Crime>
    
    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): Crime?
}
```

- @Dao 애노테이션을 지정하여 CrimeDao가 DAO 중 하나임을 Room이 알게 한다.
- CrimeDao 인터페이스를 데이터베이스 클래스에 등록해야 한다.

```kotlin
@Database(entities = [ Crime::class ], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDataBase() {
    
    abstract fun crimeDao(): CrimeDao
}
```

> 이제는 데이터베이스가 생성되면 우리가 사용할 수 있는 CrimeDao를 구현 클래스를 Room이 생성한다. 
> 따라서 CrimeDao의 참조를 가지면 CrimeDao에 정의된 함수들을 호출해서 데이터베이스를 사용할 수 있다.

## 리포지토리 패턴으로 데이터베이스 액세스하기
- 리포지토리 클래스는 리포지터리(데이터 저장소)를 구현한다.
- 단일 또는 여러 소스로부터 데이터를 엑세스 하는 로직을 캡슐화하고, 로컬 데이터베이스나 원격 서버로부터 특정 데이터셋을 가져오거나 저장하는 방법을 결정한다.

```kotlin
class CrimeRepository private  constructor(context: Context) {
    
    companion object{
        private var INSTANCE: CrimeRepository? = null
        
        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }
        
        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
```

> 레포지토리는 싱글톤이다. 즉, 앱이 실행되는 동안 하나의 인스턴스만 생성된다는 의미다.
> 싱글톤으로 구현하게 되면 액티비티나 프래그먼트와 같은 특정 생명주기에 종속되지 않고 앱이 메모리에서 제거되면 같이 소멸된다.

### Application의 서브클래스로 앱이 시작하고 최초로 해야할 작업을 지정해주기

```kotlin
class CriminalIntentApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}
```

> 매니페스트에 등록을 해줘야 정상 작동한다.

### 리포지토리 속성 설정하기(CirmeRopository.kt)
```kotlin
private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {
    
    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()
    
    private val crimeDao = database.crimeDao()
    
    fun getCrimes(): List<Crime> = crimeDao.getCrimes()
    
    fun getCrime(id: UUID): Crime? = crimeDao.getCrime(id)
    
    ...
}
```

> Room은 DAO에 쿼리를 구현하므로 리포지토리에 DAO의 함수를 호출하는 함수가 필요하다. 

## 애플리케이션의 스레드
- 데이터베이스로부터 데이터를 가져오는 것은 즉시 처리되지 않고 오래 걸릴 수 있다. Room은 메인 스레드에서의 데이터베이스 엑세스를 허용하지 않는다.
- 스레드는 단일의 실행 시퀀스이다.
- 스레드 내부의 코드는 한 단계씩 실행된다.
- 모든 안드로이드 앱은 main 스레드로 시작된다.
- 그러나 main 스레드는 미리 정해진 순서로 실행되지 않는다.
- 대신 무한 루프에 머물면서 사용자나 시스템이 유발한 이벤트를 기다린다. 그리고 이벤트가 발생하면 응답하는 코드를 실행한다.
- main 스레드는 UI를 변경하는 모든 코드를 실행하며, 이벤트들은 어떤 형태로든 모두 UI와 관련이 있어서 main 스레드를 UI 스레드라고도 한다.

## 백그라운드 스레드
- main 스레드가 중요한 이벤트에 대한 응답에 실패 했다고 안드로이드의 와치독이 판단하면 ANR이 발생한다.
- 장시간 실행되는 모든 작업은 백그라운드 스레드로 수행되어야 한다.
- UI는 main 스레드에서만 변경할 수 있다.

## LiveData
- Room DAO의 쿼리에서 LiveData를 반환하도록 구성하면, Room이 백그라운드 스레드에서 쿼리 작업을 자동실행한 후 그 결과를 LiveData로 객체로 반환한다.
- 그리고 뷰단(Activity, Fragment)에서는 LiveData 객체를 관찰하도록 설정만 하면 된다.

```kotlin
private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {
    ...
    
    private val crimeDao = database.crimeDao()
    
    fun getCrimes(): ListData<List<Crime>> = crimeDao.getCrimes()
    
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)
    
    ...
}
```

### LiveData.observe(LifeCycleOwner, Observer) 함수 
- LiveData 인스턴스에 옵저버를 등록하기 위해 사용된다.
- LifecycleOwner : 이걸 지정해주면서 옵저버의 생명주기를 한정시킨다.
- Observer: LiveData의 새로운 데이터를 처리한다.

> LifeData는 프래그먼트 뷰의 생명주기에 따라 반응해서 이런 컴포넌트를 생명주기-인식 컴포넌트라고 한다.(lifecycle-aware component)
