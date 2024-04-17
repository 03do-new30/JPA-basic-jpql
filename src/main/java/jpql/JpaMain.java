package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        //code
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 영속성 컨텍스트 비운다
            em.flush();
            em.clear();

            // join 쿼리 나감
            List<Team> result1 = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();
            // join 쿼리가 나간다면 jpql에도 명시해 주는 것이 좋음
            // 예측 가능하게 짜야 한다!
            List<Team> result2 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            tx.commit(); // 커밋 시점에 INSERT (버퍼링 가능)
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close(); // 영속성 컨텍스트를 종료
        }
        emf.close();
    }
}
