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

//            String query = "select t from Team t join fetch t.members";
            // 일반 쿼리
            // SQL문에 join은 들어가지만, 실제 데이터를 퍼올리는 건 Team에 대해서만 퍼올림
            String query = "select t from Team t join t.members";
            List<Team> resultList = em.createQuery(query, Team.class).getResultList();
            System.out.println("resultList.size() = " + resultList.size());
            // Hibernate6 변경 사항
            // distinct 명령어를 사용하지 않아도 애플리케이션에서 중복 제거가 자동으로 적용됨
            for (Team team : resultList) {
                // 일반 쿼리를 사용하면 team.getMembers()를 위해 쿼리가 추가로 실행됨
                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("    member = " + member);
                }
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
