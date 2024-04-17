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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            // 영속성 컨텍스트 비운다
            em.flush();
            em.clear();

//            String query = "select m from Member m inner join m.team t"; // inner join
//            String query = "select m from Member m left outer join m.team t"; // left outer join
//            String query = "select m from Member m, Team t where m.username = t.name"; // 세타 조인
            // on절을 활용한 조인 쿼리
            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
            List<Member> resultList = em.createQuery(query, Member.class).getResultList();
            // on절을 활용한 조인 쿼리 - 연관관계 없는 엔티티 외부(left outer join) 조인
            // 문법이 살짝 다름 Team t 따로 선언해야 함
            String query2 = "select m from Member m left join Team t on m.username = t.name";
            List<Member> resultList2 = em.createQuery(query2, Member.class).getResultList();
            for (Member member1 : resultList2) {
                System.out.println("member1 = " + member1);
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
