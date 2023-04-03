package com.quickbill.services;

import com.quickbill.models.Customer;
import com.quickbill.models.Invoice;
import com.quickbill.models.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class DbService {

    SessionFactory sessionFactory;
    Session session;

    public DbService() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public void addUser(String login, String password1, String email) {
        try {
            User user = new User(login, password1, email);
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        }
    }

    public User getUser(String username) {
        try {
            session.beginTransaction();
            String hql = "from User where username = :username or email = :username";
            Query query = session.createQuery(hql);
            query.setParameter("username", username);
            User user = (User) query.uniqueResult();
            session.getTransaction().commit();
            return user;
        } catch (HibernateException e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return null;
        }

    }

    public void addInvoice(Invoice invoice) {
        try {
            session.beginTransaction();
            System.out.println(invoice.getUser().getUsername());
            session.save(invoice);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        }
    }

    public List<Invoice> getInvoices(long userID) {
        try {
            session.beginTransaction();
            String hql = "FROM Invoice i WHERE i.user.id = " + userID+" ORDER BY i.id DESC";
            Query query = session.createQuery(hql);
            query.setMaxResults(1000);
            List<Invoice> list = query.list();
            session.getTransaction().commit();
            return list;
        } catch (HibernateException e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return null;
        }
    }

    public void editInvoice(Invoice invoice) {
        try {
            session.beginTransaction();
            System.out.println(invoice.getItems().size());
            session.persist(invoice);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        }
    }

    public void deleteInvoice(Invoice invoice) {
        try {
            session.beginTransaction();
            session.delete(invoice);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        }
    }

    public Customer getCustomer(long nip) {
        try {
            session.beginTransaction();
            String hql = "from Customer where NIP = :nip order by id desc";
            Query query = session.createQuery(hql);
            query.setMaxResults(1);
            query.setParameter("nip", nip);
            Customer customer = (Customer) query.uniqueResult();
            session.getTransaction().commit();
            return customer;
        } catch (HibernateException e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return null;
        }
    }
}
