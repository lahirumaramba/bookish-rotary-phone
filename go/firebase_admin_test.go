package main

import (
	"context"
	"fmt"
	"testing"

	firebase "firebase.google.com/go/v4"
	"google.golang.org/api/iterator"
)

var app *firebase.App

func Test1InitializeApp(t *testing.T) {
	ctx := context.Background()

	config := &firebase.Config{}

	var err error
	app, err = firebase.NewApp(ctx, config)
	if err != nil {
		t.Fatalf("error initializing app: %v", err)
	}

	if app == nil {
		t.Fatal("app is nil")
	}

	fmt.Println("Firebase Admin SDK initialized successfully via ADC")
}

func Test2AuthGetUsers(t *testing.T) {
	ctx := context.Background()
	client, err := app.Auth(ctx)
	if err != nil {
		t.Fatalf("error getting Auth client: %v", err)
	}

	pager := client.Users(ctx, "")
	user, err := pager.Next()
	if err != iterator.Done && err != nil {
		t.Fatalf("error listing users: %v", err)
	}

	if user != nil {
		fmt.Println("Successfully fetched users.")
	} else {
		fmt.Println("No users fetched (iterator empty).")
	}
}

func Test3FirestoreRead(t *testing.T) {
	ctx := context.Background()
	client, err := app.Firestore(ctx)
	if err != nil {
		t.Fatalf("error getting Firestore client: %v", err)
	}
	defer client.Close()

	doc, err := client.Collection("wif-demo").Doc("test-connection").Get(ctx)
	if err != nil {
		t.Fatalf("error reading Firestore document: %v", err)
	}

	if !doc.Exists() {
		t.Fatal("expected document to exist")
	}

	data := doc.Data()
	if data["message"] != "Hello from GitHub Actions WIF!" {
		t.Errorf("unexpected message: %v", data["message"])
	}
	if data["timestamp"] == nil {
		t.Error("expected timestamp to be present")
	}

	fmt.Println("Firestore read successful.")
}

func Test4CreateCustomToken(t *testing.T) {
	ctx := context.Background()
	client, err := app.Auth(ctx)
	if err != nil {
		t.Fatalf("error getting Auth client: %v", err)
	}

	uid := "wif-demo-user-123"
	token, err := client.CustomToken(ctx, uid)
	if err != nil {
		t.Fatalf("error creating custom token: %v", err)
	}

	if token == "" {
		t.Fatal("expected custom token to be non-empty string")
	}

	fmt.Println("Successfully created a custom signed token.")
}
