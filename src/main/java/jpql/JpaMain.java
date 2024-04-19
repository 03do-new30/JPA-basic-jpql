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

            // 영속성 컨텍스트 비운다
            em.flush();
            em.clear();

            // 엔티티 직접 사용 - 기본 키 값으로 사용된다
            // (1) 엔티티를 파라미터로 전달
            String query = "select m from Member m where m = :member";
            Member findMember = em.createQuery(query, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            System.out.println("findMember = " + findMember);
            // (2) 식별자로 직접 전달
            String query2 = "select m from Member m where m.id = :member";
            Member findMember2 = em.createQuery(query2, Member.class)
                    .setParameter("member", member1.getId())
                    .getSingleResult();
            System.out.println("findMember = " + findMember2);
            // 둘 다 똑같은 쿼리

            // 엔티티 직접 사용 - 외래 키 값
            // TEAM_ID = 팀아이디
            // (1) 엔티티 전달
            String query3 = "select m from Member m where m.team = :team";
            List<Member> resultList = em.createQuery(query3, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();
            for (Member member : resultList) {
                System.out.println("member = " + member);
            }
            // (2) 식별자 전달
            String query4 = "select m from Member m where m.team.id = :teamId";
            List<Member> resultList1 = em.createQuery(query4, Member.class)
                    .setParameter("teamId", teamA.getId())
                    .getResultList();
            for (Member member : resultList1) {
                System.out.println("member = " + member);
            }
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
