import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Presidente } from './presidente';

describe('Presidente', () => {
  let component: Presidente;
  let fixture: ComponentFixture<Presidente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Presidente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Presidente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
