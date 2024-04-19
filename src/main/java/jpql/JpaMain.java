package jpql;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        //code
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Team teamC = new Team();
            teamC.setName("팀C");
            em.persist(teamC);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("회원4");
            em.persist(member4);


            // 벌크 연산
            // 모든 회원의 나이를 20살로 바꿔보자
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate(); // 쿼리 날리면 자동으로 flush! (flush된다고 영속성 컨텍스트가 지워지는 건 아님, 그저 DB 반영)
            System.out.println("resultCount = " + resultCount);
            // 벌크 연산은 영속성 컨텍스트를 무시
            // 해결방법1. 벌크연산을 먼저 실행
            // 해결방법2. 벌크 연산 수행 후 영속성 컨텍스트 초기화한다


            // 아래 멤버들은 전부 age 0으로 나온다
            // 벌크 연산은 DB에 강제로 업데이트 하는 것이기 때문에 영속성 컨텍스트를 무시한다
            // 따라서, 현재 영속성 컨텍스트에 있는 member1, 2, 3, 4의 age는 20으로 업데이트 전의 age이다.
            System.out.println("member1 = " + member1);
            System.out.println("member2 = " + member2);
            System.out.println("member3 = " + member3);
            System.out.println("member4 = " + member4);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close(); // 영속성 컨텍스트를 종료
        }
        emf.close();
    }
}
