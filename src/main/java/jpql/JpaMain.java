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

            // 결과가 하나 이상일 때
            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            // 결과가 정확히 하나
            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id = 100L", Member.class);
            try{
                Member singleResult = query2.getSingleResult();
            } catch(NoResultException e) {
                System.out.println("no result exception");
            }
            // 주의! 결과가 없으면 NoResultException
            // 결과가 둘 이상이면 NonUniqueResultException


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
