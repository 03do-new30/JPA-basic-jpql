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
            for (int i = 0; i < 100; i ++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }


            // 영속성 컨텍스트 비운다
            em.flush();
            em.clear();

            // 페이징 쿼리
            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("resultList.size() = " + resultList.size());
            for (Member member1 : resultList) {
                System.out.println(member1);
            }

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
